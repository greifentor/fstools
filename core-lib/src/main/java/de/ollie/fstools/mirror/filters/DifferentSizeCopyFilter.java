package de.ollie.fstools.mirror.filters;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.mirror.CopyFilter;

/**
 * A filter to check for different sizes of source and target file.
 *
 * @author ollie (14.09.2020)
 */
public class DifferentSizeCopyFilter implements CopyFilter {

	@Override
	public boolean isToCopy(FileStats sourceFileStats, FileStats targetFileStats) {
		return sourceFileStats.getSize() != targetFileStats.getSize();
	}

}