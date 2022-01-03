package de.ollie.fstools.shell;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.shell.service.BuildActionListEvent;
import de.ollie.fstools.shell.service.BuildActionListObserver;
import de.ollie.fstools.shell.service.FSToolsService;
import de.ollie.fstools.shell.service.ProcessMirrorActionsEvent;
import de.ollie.fstools.shell.service.ProcessMirrorActionsObserver;
import de.ollie.fstools.shell.service.TouchService;
import de.ollie.fstools.shell.service.so.MirrorActionSO;
import de.ollie.fstools.shell.service.so.MirrorActionSO.ActionTypeSO;

/**
 * The shell component for the fstools.
 *
 * @author ollie (11.09.2020)
 */
@ShellComponent
public class FSToolsShellComponent {

	@Autowired
	private FSToolsService fsToolsService;
	@Autowired
	private TouchService touchService;

	@ShellMethod("Shows a list of necessary actions to fit the content of the target path to that of the source path.")
	public String cmp(String sourcePathName, String targetPathName, @ShellOption(defaultValue = "") String excludes,
			@ShellOption(defaultValue = "") String copyAtAnyTime) {
		try {
			return mirrorActionSOsToStringTable(
					loadMirrorActions(sourcePathName, targetPathName, excludes, copyAtAnyTime));
		} catch (Exception e) {
			e.printStackTrace();
			return "error: " + e.getMessage();
		}
	}

	private List<MirrorActionSO> loadMirrorActions(String sourcePathName, String targetPathName, String excludes,
			String copyAtAnyTime) throws IOException {
		List<String> excludePatterns = getListFromCommaSeparatedString(excludes);
		List<String> copyAtAnyTimePatterns = getListFromCommaSeparatedString(copyAtAnyTime);
		Counter countFiles = new Counter();
		Counter countFolders = new Counter();
		String format = "folder: %-6d, files: %-6d";
		System.out.print(String.format(format, countFolders.getCount(), countFiles.inc().getCount()));
		BuildActionListObserver observer = new BuildActionListObserver() {

			@Override
			public void fileDetected(BuildActionListEvent event) {
				System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
				System.out.print(String.format(format, countFolders.getCount(), countFiles.inc().getCount()));
			}

			@Override
			public void folderDetected(BuildActionListEvent event) {
				System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
				System.out.print(String.format(format, countFolders.inc().getCount(), countFiles.getCount()));
			}

		};
		List<MirrorActionSO> actions = fsToolsService.buildActionList(sourcePathName, targetPathName, observer,
				excludePatterns, copyAtAnyTimePatterns);
		System.out.println();
		return actions;
	}

	private List<String> getListFromCommaSeparatedString(String s) {
		String[] a = StringUtils.split(s, ",");
		if (a == null) {
			return new ArrayList<>();
		}
		return Arrays.asList(a);
	}

	private String mirrorActionSOsToStringTable(List<MirrorActionSO> actions) {
		StringBuilder sb = new StringBuilder();
		long totalSize = 0;
		int filesToCopy = 0;
		int filesToRemove = 0;
		for (MirrorActionSO action : actions) {
			if (action.getType() == ActionTypeSO.COPY) {
				sb.append(String.format("%-8s %s -> %s%n", String.valueOf(action.getType()), action.getSourceFileName(),
						action.getTargetFileName()));
				totalSize += action.getSourceFileSizeInBytes();
				filesToCopy++;
			}
			if (action.getType() == ActionTypeSO.REMOVE) {
				sb.append(String.format("%-8s %s%n", String.valueOf(action.getType()), action.getTargetFileName()));
				filesToRemove++;
			}
		}
		sb.append("total (bytes): " + totalSize + " - copies: " + filesToCopy + ", removes: " + filesToRemove);
		return sb.toString();
	}

	@ShellMethod("Shows the file stats for the passed path")
	public String fs(String path) {
		try {
			return fileStatsToString(fsToolsService.getFileStats(path));
		} catch (Exception e) {
			return "error: " + e.getMessage();
		}
	}

	private String fileStatsToString(FileStats fileStats) {
		return String.format("%s%n" //
				+ "%s%n" //
				+ "%d bytes%n" //
				+ "modified at %s%n", //
				fileStats.getName(), //
				String.valueOf(fileStats.getType()), //
				fileStats.getSize(), //
				String.valueOf(fileStats.getLastModifiedTime()));
	}

	@ShellMethod("Shows the file stats of all file in the path")
	public String ls(@ShellOption(defaultValue = ".") String path) {
		try {
			String[] pathes = new File(path).list();
			return fileStatsToStringTable(path, pathes);
		} catch (Exception e) {
			return "error: " + e.getMessage();
		}
	}

	private String fileStatsToStringTable(String basePath, String[] pathes) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (String path : pathes) {
			FileStats fileStats = fsToolsService
					.getFileStats(basePath.replace("\\", "/") + "/" + path.replace("\\", "/"));
			sb.append(String.format("%-10s %10d %20s %s%n", String.valueOf(fileStats.getType()), fileStats.getSize(),
					fileStats.getLastModifiedTime().toString(), fileStats.getName()));
		}
		return sb.toString();
	}

	@ShellMethod("Mirrors source path to the target path.")
	public String mirror(String sourcePathName, String targetPathName, @ShellOption(defaultValue = "") String excludes,
			@ShellOption(defaultValue = "") String copyAtAnyTime,
			@ShellOption(defaultValue = "104857600") int minFileSizeForCopier) {
		try {
			System.out.println(sourcePathName + " -> " + targetPathName);
			Counter actionCount = new Counter();
			Counter copied = new Counter();
			Counter errors = new Counter();
			Counter removed = new Counter();
			File sourcePath = new File(sourcePathName);
			if (!sourcePath.exists() || (sourcePath.listFiles().length == 0)) {
				System.out.println("Source path does not exists or is empty! Should process be stopped? (Y/N)");
				Scanner in = new Scanner(System.in);
				String answer = in.nextLine();
				if (!"y".equalsIgnoreCase(answer) ^ !"yes".equalsIgnoreCase(answer)) {
					return "Operation aborted!";
				}
			}
			List<MirrorActionSO> actions = loadMirrorActions(sourcePathName, targetPathName, excludes, copyAtAnyTime);
			ProcessMirrorActionsObserver observer = new ProcessMirrorActionsObserver() {

				private String lastTargetFileName;

				@Override
				public void copying(ProcessMirrorActionsEvent event) {
					actionCount.inc();
					System.out.print(getPercentileString(actions.size(), actionCount.getCount()) + "copying "
							+ event.getSourceFileName() + " to " + event.getTargetFileName());
				}

				@Override
				public void copied(ProcessMirrorActionsEvent event) {
					System.out.println(" ok");
					copied.inc();
				}

				@Override
				public void partialCopied(ProcessMirrorActionsEvent event) {
					if (event.getTargetFileName().equals(lastTargetFileName)) {
						System.out.print("\b\b\b\b\b\b\b\b\b\b");
					}
					long bytesCopied = event.getBytesTotal() - event.getBytesLeft();
					double percentWrote = (100.0 / (double) event.getBytesTotal()) * bytesCopied;
					System.out.print(String.format(" (%6.2f%%)", percentWrote));
					lastTargetFileName = event.getTargetFileName();
//					System.out.println(getPercentileString(actions.size(), actionCount.getCount()) + "partial copied "
//							+ "(" + event.getBytesLeft() + ") " + event.getTargetFileName());
				}

				@Override
				public void removing(ProcessMirrorActionsEvent event) {
					actionCount.inc();
					System.out.print(getPercentileString(actions.size(), actionCount.getCount()) + "removing "
							+ event.getTargetFileName());
				}

				@Override
				public void removed(ProcessMirrorActionsEvent event) {
					System.out.println(" ok");
					removed.inc();
				}

				@Override
				public void errorDetected(ProcessMirrorActionsEvent event) {
					errors.inc();
				}

			};
			fsToolsService.processMirrorActions(actions, observer, minFileSizeForCopier);
			return "done (copied: " + copied.getCount() + ", removed: " + removed.getCount() + ", errors: "
					+ errors.getCount() + ")";
		} catch (Exception e) {
			e.printStackTrace();
			return "error: " + e.getMessage();
		}
	}

	private String getPercentileString(int total, int count) {
		double percentiles = ((double) count / (double) total) * 100;
		return String.format("(%6.2f%%) - ", percentiles);
	}

	@ShellMethod("Mirrors source path to the target path.")
	public String touch(String sourcePathName, @ShellOption(defaultValue = "now") String timestamp,
			@ShellOption(defaultValue = "false") boolean recursive) {
		LocalDateTime lastModifiedDate = LocalDateTime.now();
		if (!timestamp.equalsIgnoreCase("now")) {
			lastModifiedDate = LocalDateTime.parse(timestamp);
		}
		touchFile(sourcePathName, lastModifiedDate, recursive);
		return sourcePathName + " last modified set to: " + lastModifiedDate;
	}

	private void touchFile(String fileName, LocalDateTime timestamp, boolean recursive) {
		File f = new File(fileName);
		if (f.list() != null) {
			for (String fn : f.list()) {
				if (new File(fn).isDirectory() && recursive) {
					touchFile(fn, timestamp, recursive);
				} else {
					touchFile(fileName, timestamp);
				}
			}
		} else {
			touchFile(fileName, timestamp);
		}
	}

	private void touchFile(String fn, LocalDateTime timestamp) {
		try {
			System.out.println(fn + " new last modified time: " + timestamp);
			touchService.touch(fn, timestamp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}