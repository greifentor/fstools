package de.ollie.fstools.shell;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.shell.service.FSToolsService;
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

	@ShellMethod("Shows a list of necessary actions to fit the content of the target path to that of the source path.")
	public String cmp(String sourcePathName, String targetPathName, @ShellOption(defaultValue = "") String excludes) {
		try {
			List<MirrorActionSO> actions = fsToolsService.buildActionList(sourcePathName, targetPathName);
			return mirrorActionSOsToStringTable(actions);
		} catch (Exception e) {
			return "error: " + e.getMessage();
		}
	}

	private String mirrorActionSOsToStringTable(List<MirrorActionSO> actions) {
		StringBuilder sb = new StringBuilder();
		for (MirrorActionSO action : actions) {
			if (action.getType() == ActionTypeSO.COPY) {
				sb.append(String.format("%-8s %s -> %s%n", String.valueOf(action.getType()), action.getSourceFileName(),
						action.getTargetFileName()));
			}
			if (action.getType() == ActionTypeSO.REMOVE) {
				sb.append(String.format("%-8s %s%n", String.valueOf(action.getType()), action.getTargetFileName()));
			}
		}
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
			sb.append(String.format("%-10s %10d %s%n", String.valueOf(fileStats.getType()), fileStats.getSize(),
					fileStats.getName()));
		}
		return sb.toString();
	}

}