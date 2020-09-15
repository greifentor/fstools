package de.ollie.fstools.mirror.filters;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.mirror.CopyFilter;

/**
 * A filter for files which are to copy at any mirror process independently from other filters (except an exclusion
 * maybe).
 *
 * @author ollie (15.09.2020)
 */
public class CopyAtAnyTimeFileNamePatternCopyFilter implements CopyFilter {

	private String fileNamePattern;

	public CopyAtAnyTimeFileNamePatternCopyFilter(String fileNamePattern) {
		super();
		this.fileNamePattern = fileNamePattern;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean isToCopy(FileStats sourceFileStats, FileStats targetFileStats) {
		return sourceFileStats.getName().toLowerCase().contains(fileNamePattern.toLowerCase());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}