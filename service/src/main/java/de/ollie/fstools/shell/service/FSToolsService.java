package de.ollie.fstools.shell.service;

import java.io.IOException;
import java.util.List;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.shell.service.so.MirrorActionSO;

/**
 * An interface for the fstools service.
 *
 * @author ollie (11.09.2020)
 */
public interface FSToolsService {

	/**
	 * Returns a list of actions which are necessary to change the target path with regards to content of the source
	 * path.
	 * 
	 * @param sourcePathName          The source path.
	 * @param targetPathName          The target path.
	 * @param buildActionListObserver An observer to get noticed of the current progress of action list building (or
	 *                                null, if no notification requested.
	 * @param excludePatterns         A comma separated list of name patterns which are to exclude from copy process at
	 *                                any time.
	 * @param copyAtAnyTimePatterns   A comma separated list of name patterns for files which are to copy at any
	 *                                process.
	 * @return A list list of actions which are necessary to change the target path with regards to content of the
	 *         source path or an empty list if both paths are of equal content.
	 * @throws IOException If something gets wrong while reading the file information.
	 */
	List<MirrorActionSO> buildActionList(String sourcePathName, String targetPathName,
			BuildActionListObserver buildActionListObserser, List<String> excludePatterns,
			List<String> copyAtAnyTimePatterns) throws IOException;

	/**
	 * Returns the information for the file with the passed path.
	 * 
	 * @param pathName The path of the file whose information are to return.
	 * @return The information of the file whose path has been passed.
	 * @throws IOException In case of an error occurs while reading the file stats.
	 */
	FileStats getFileStats(String pathName) throws IOException;

	/**
	 * Processes the passed mirror actions.
	 * 
	 * @param mirrorActions                The mirror actions to process.
	 * @param processMirrorActionsObserver An observer for mirror action processing progress observing.
	 * @throws IOException If an error occurs while processing the mirror actions.
	 */
	void processMirrorActions(List<MirrorActionSO> actions, ProcessMirrorActionsObserver processMirrorActionsObserver)
			throws IOException;

}