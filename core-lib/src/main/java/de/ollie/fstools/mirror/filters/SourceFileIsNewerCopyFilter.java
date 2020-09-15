package de.ollie.fstools.mirror.filters;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.mirror.CopyFilter;

/**
 * Checks if the source file has been changed after the target.
 *
 * @author ollie (14.09.2020)
 */
public class SourceFileIsNewerCopyFilter extends BaseFilter implements CopyFilter {

	@Override
	public boolean isToCopy(FileStats sourceFileStats, FileStats targetFileStats) {
		return sourceFileStats.getLastModifiedTime().isAfter(targetFileStats.getLastModifiedTime());
	}

}