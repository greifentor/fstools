package de.ollie.fstools.mirror;

/**
 * A filter to exclude mirror actions from a build list.
 *
 * @author ollie (14.09.2020)
 */
public interface ExcludeActionFilter {

	/**
	 * Checks if a mirror action is to exclude from mirror process.
	 * 
	 * @param mirrorAction The mirror action to check for.
	 * @return "true" if the passed mirror action is to exclude from the mirror process, "false" otherwise.
	 */
	boolean isToExclude(MirrorAction mirrorAction);

}