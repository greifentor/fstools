package de.ollie.fstools.mirror.filters;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.mirror.CopyFilter;

/**
 * A copy filter which checks for file where the source file exists only.
 *
 * @author ollie (14.09.2020)
 */
public class TargetDoesNotExistsCopyFilter implements CopyFilter {

	@Override
	public boolean isToCopy(FileStats sourceFileStats, FileStats targetFileStats) {
		return targetFileStats == null;
	}

}