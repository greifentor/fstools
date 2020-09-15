package de.ollie.fstools.mirror;

import static de.ollie.utils.Check.ensure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.filestats.FileStats.FileType;
import de.ollie.fstools.filestats.FileStatsReader;
import de.ollie.fstools.mirror.MirrorAction.ActionType;
import de.ollie.fstools.mirror.MirrorAction.DifferenceType;
import de.ollie.fstools.mirror.filters.DifferentSizeCopyFilter;
import de.ollie.fstools.mirror.filters.SourceFileIsNewerCopyFilter;
import de.ollie.fstools.mirror.filters.TargetDoesNotExistsCopyFilter;

/**
 * A class which is able to compare to folder (including sub folders) and create a list of actions to equalize the
 * target folder to the source folder.
 *
 * @author Oliver.Lieshoff (06.09.2020)
 *
 */
@Component
public class ActionListBuilder {

	private static List<CopyFilter> basicCopyFilters = new ArrayList<>(Arrays.asList( //
			new TargetDoesNotExistsCopyFilter(), //
			new SourceFileIsNewerCopyFilter(), //
			new DifferentSizeCopyFilter() //
	));

	private FileStatsReader fileStatsReader = new FileStatsReader();

	/**
	 * Returns a list of actions to equalize the passed target folder to the passed source folder:
	 * 
	 * <P>
	 * * File existing in the source folder only causes a COPY action. <BR>
	 * * File existing in both folder but is more recent in the source folder causes a COPY action. <BR>
	 * * File existing in the target folder only, causes a REMOVE action.
	 * 
	 * @param sourcePathName        The name of the source path.
	 * @param targetPathName        The name of the target path.
	 * @param additionalCopyFilters Additional copy filters, if necessary.
	 * @param excludeActionFilters  Filters to exclude mirror actions.
	 * @return A list of action to equalize the passed target folder to the passed source folder.
	 * @throws IOException If an error occurs while building up the list.
	 */
	public List<MirrorAction> build(String sourcePathName, String targetPathName,
			List<CopyFilter> additionalCopyFilters, ExcludeActionFilter... excludeActionFilters) throws IOException {
		ensure(sourcePathName != null, new NullPointerException("source path name cannot be null."));
		ensure(targetPathName != null, new NullPointerException("target path name cannot be null."));
		ensure(additionalCopyFilters != null, new NullPointerException("additional copy filters cannot be null."));
		List<MirrorAction> actions = new ArrayList<>();
		return fillMirrorActions(actions, sourcePathName, targetPathName, getCopyFilters(additionalCopyFilters),
				excludeActionFilters);
	}

	private List<CopyFilter> getCopyFilters(List<CopyFilter> additionalCopyFilters) {
		List<CopyFilter> copyFilters = new ArrayList<>(additionalCopyFilters);
		copyFilters.addAll(basicCopyFilters);
		return copyFilters;
	}

	private List<MirrorAction> fillMirrorActions(List<MirrorAction> actions, String sourceFolderName,
			String targetFolderName, List<CopyFilter> copyFilters, ExcludeActionFilter... excludeActionFilters)
			throws IOException {
		sourceFolderName = completePath(sourceFolderName);
		targetFolderName = completePath(targetFolderName);
		processSourceFiles(actions, sourceFolderName, targetFolderName, copyFilters);
		processTargetFiles(actions, sourceFolderName, targetFolderName, copyFilters);
		actions = cleanUpMirrorActionsByExcludeFilters(actions, excludeActionFilters);
		return actions;
	}

	private void processSourceFiles(List<MirrorAction> actions, String sourceFolderName, String targetFolderName,
			List<CopyFilter> copyFilters) throws IOException {
		List<String> sourceFileNames = getFileNameList(sourceFolderName);
		for (String fileName : sourceFileNames) {
			FileStats sourceFileStats = getFileStats(sourceFolderName + fileName);
			if (sourceFileStats.getType() == FileType.DIRECTORY) {
				fillMirrorActions(actions, sourceFolderName + fileName, targetFolderName + fileName, copyFilters);
			} else if (sourceFileStats.getType() == FileType.FILE) {
				FileStats targetFileStats = getFileStats(targetFolderName + fileName);
				if (isFileToCopy(sourceFileStats, targetFileStats, copyFilters)) {
					actions.add(new MirrorAction() //
							.setDifferenceType(getDifferenceTypeOfFileToCopy(sourceFileStats, targetFileStats)) //
							.setSourceFileName(sourceFileStats.getName()) //
							.setTargetFileName(targetFileStats != null //
									? targetFileStats.getName() //
									: targetFolderName + fileName) //
							.setType(ActionType.COPY) //
					);
				}
			}
		}
	}

	private void processTargetFiles(List<MirrorAction> actions, String sourceFolderName, String targetFolderName,
			List<CopyFilter> copyFilters) throws IOException {
		List<String> targetFileNames = getFileNameList(targetFolderName);
		for (String fileName : targetFileNames) {
			FileStats sourceFileStats = getFileStats(sourceFolderName + fileName);
			FileStats targetFileStats = getFileStats(targetFolderName + fileName);
			if (doesNotExistInSource(sourceFileStats)) {
				actions.add(new MirrorAction() //
						.setTargetFileName(targetFileStats != null ? targetFileStats.getName() : null) //
						.setType(ActionType.REMOVE) //
				);
			}
		}
	}

	private List<MirrorAction> cleanUpMirrorActionsByExcludeFilters(List<MirrorAction> actions,
			ExcludeActionFilter... excludeActionFilters) {
		return actions //
				.stream() //
				.filter(mirrorAction -> !isToExclude(mirrorAction, excludeActionFilters)) //
				.collect(Collectors.toList()) //
		;
	}

	private boolean isToExclude(MirrorAction mirrorAction, ExcludeActionFilter... excludeActionFilters) {
		return Arrays.asList(excludeActionFilters) //
				.stream() //
				.anyMatch(excludeActionFilter -> excludeActionFilter.isToExclude(mirrorAction)) //
		;
	}

	private String completePath(String pn) {
		return pn != null && !pn.endsWith("//") ? pn.concat("/") : pn;
	}

	private List<String> getFileNameList(String path) {
		String[] fileNames = new File(path).list();
		if (fileNames == null) {
			return new ArrayList<>();
		}
		return Arrays.asList(fileNames);
	}

	private FileStats getFileStats(String fileName) throws IOException {
		try {
			return fileStatsReader.read(fileName);
		} catch (Exception e) {
			return null;
		}
	}

	private boolean isFileToCopy(FileStats sourceFileStats, FileStats targetFileStats, List<CopyFilter> copyFilters) {
		return copyFilters //
				.stream() //
				.anyMatch(copyFilter -> copyFilter.isToCopy(sourceFileStats, targetFileStats)) //
		;
	}

	private DifferenceType getDifferenceTypeOfFileToCopy(FileStats sourceFileStats, FileStats targetFileStats) {
		if (targetFileStats == null) {
			return DifferenceType.EXISTENCE;
		} else if (sourceFileStats.getSize() != targetFileStats.getSize()) {
			return DifferenceType.SIZE;
		} else if (sourceFileStats.getLastModifiedTime().isAfter(targetFileStats.getLastModifiedTime())) {
			return DifferenceType.TIME;
		}
		return null;
	}

	private boolean doesNotExistInSource(FileStats sourceFileStats) {
		return sourceFileStats == null;
	}

	public static void main(String[] args) {
		ActionListBuilder builder = new ActionListBuilder();
		try {
			builder.build(args[0], args[1], new ArrayList<>()) //
					.forEach(System.out::println);
			;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}