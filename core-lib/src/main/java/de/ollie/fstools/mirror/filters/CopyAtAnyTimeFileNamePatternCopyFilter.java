package de.ollie.fstools.mirror.filters;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.mirror.CopyFilter;

/**
 * A filter for files which are to copy at any mirror process independently from other filters (except an exclusion
 * maybe).
 *
 * @author ollie (15.09.2020)
 */
public class CopyAtAnyTimeFileNamePatternCopyFilter extends BaseFilter implements CopyFilter {

	private String fileNamePattern;

	public CopyAtAnyTimeFileNamePatternCopyFilter(String fileNamePattern) {
		super();
		this.fileNamePattern = fileNamePattern;
	}

	@Override
	public boolean isToCopy(FileStats sourceFileStats, FileStats targetFileStats) {
		return sourceFileStats.getName().toLowerCase().contains(fileNamePattern.toLowerCase());
	}

}