package de.ollie.fstools.mirror;

import static de.ollie.utils.Check.ensure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.filestats.FileStats.FileType;
import de.ollie.fstools.filestats.FileStatsReader;
import de.ollie.fstools.mirror.MirrorAction.ActionType;

/**
 * A class which is able to compare to folder (including sub folders) and create a list of actions to equalize the
 * target folder to the source folder.
 *
 * @author Oliver.Lieshoff (06.09.2020)
 *
 */
public class ActionListBuilder {

	private FileStatsReader fileStatsReader = new FileStatsReader();

	/**
	 * Returns a list of actions to equalize the passed target folder to the passed source folder:
	 * 
	 * <P>
	 * * File existing in the source folder only causes a COPY action. <BR>
	 * * File existing in both folder but is more recent in the source folder causes a COPY action. <BR>
	 * * File existing in the target folder only, causes a REMOVE action.
	 * 
	 * @param sourceFolderName The name of the source folder.
	 * @param targetFolderName The name of the target folder.
	 * @return A list of action to equalize the passed target folder to the passed source folder.
	 * @throws IOException If an error occurs while building up the list.
	 */
	public List<MirrorAction> build(String sourceFolderName, String targetFolderName) throws IOException {
		ensure(sourceFolderName != null, new NullPointerException("source folder name cannot be null."));
		ensure(targetFolderName != null, new NullPointerException("target folder name cannot be null."));
		List<MirrorAction> actions = new ArrayList<>();
		fillMirrorActions(actions, sourceFolderName, targetFolderName);
		return actions;
	}

	private void fillMirrorActions(List<MirrorAction> actions, String sourceFolderName, String targetFolderName)
			throws IOException {
		sourceFolderName = completePath(sourceFolderName);
		targetFolderName = completePath(targetFolderName);
		String[] sourceFileNames = new File(sourceFolderName).list();
		String[] targetFileNames = new File(targetFolderName).list();
		for (String fileName : sourceFileNames) {
			FileStats sourceFileStats = getFileStats(sourceFolderName + fileName);
			if (sourceFileStats.getType() == FileType.DIRECTORY) {
				fillMirrorActions(actions, sourceFolderName, targetFolderName);
			} else if (sourceFileStats.getType() == FileType.FILE) {
				FileStats targetFileStats = getFileStats(targetFolderName + fileName);
				if (isFileToCopy(sourceFileStats, targetFileStats)) {
					actions.add(new MirrorAction() //
							.setSourceFileName(sourceFileStats.getName()) //
							.setTargetFileName(targetFileStats != null //
									? targetFileStats.getName() //
									: targetFolderName + fileName) //
							.setType(ActionType.COPY) //
					);
				}
			}
		}
		if (targetFileNames != null) {
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
	}

	private String completePath(String pn) {
		return pn != null && !pn.endsWith("//") ? pn.concat("/") : pn;
	}

	private FileStats getFileStats(String fileName) throws IOException {
		try {
			return fileStatsReader.read(fileName);
		} catch (Exception e) {
			return null;
		}
	}

	private boolean isFileToCopy(FileStats sourceFileStats, FileStats targetFileStats) {
		return (targetFileStats == null)
				|| sourceFileStats.getLastModifiedTime().isAfter(targetFileStats.getLastModifiedTime())
				|| (sourceFileStats.getSize() != targetFileStats.getSize());
	}

	private boolean doesNotExistInSource(FileStats sourceFileStats) {
		return sourceFileStats == null;
	}

}