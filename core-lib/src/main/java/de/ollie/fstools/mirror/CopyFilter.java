package de.ollie.fstools.mirror;

import de.ollie.fstools.filestats.FileStats;

/**
 * An interface for filters to copy actions.
 *
 * @author ollie (14.09.2020)
 */
public interface CopyFilter {

	/**
	 * Checks if the source file ist to copy.
	 * 
	 * @param sourceFileStats The stats of the source file.
	 * @param targetFileStats The stats of the target file.
	 * @return "true" if the file is to copy from the source to the target. Otherwise "false".
	 */
	boolean isToCopy(FileStats sourceFileStats, FileStats targetFileStats);

}