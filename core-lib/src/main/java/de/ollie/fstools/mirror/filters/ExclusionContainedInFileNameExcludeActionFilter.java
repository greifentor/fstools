package de.ollie.fstools.mirror.filters;

import de.ollie.fstools.mirror.ExcludeActionFilter;
import de.ollie.fstools.mirror.MirrorAction;

/**
 * A filter for file name exclusions (means the file name contains a specific string).
 *
 * @author ollie (14.09.2020)
 */
public class ExclusionContainedInFileNameExcludeActionFilter implements ExcludeActionFilter {

	private String fileNameFragment;

	public ExclusionContainedInFileNameExcludeActionFilter(String fileNameFragment) {
		super();
		this.fileNameFragment = fileNameFragment.toLowerCase();
	}

	@Override
	public boolean isToExclude(MirrorAction mirrorAction) {
		return mirrorAction.getSourceFileName().toLowerCase().contains(fileNameFragment);
	}

}