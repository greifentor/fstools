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
	 * Returns the information for the file with the passed path.
	 * 
	 * @param pathName The path of the file whose information are to return.
	 * @return The information of the file whose path has been passed.
	 * @throws IOException In case of an error occurs while reading the file stats.
	 */
	FileStats getFileStats(String pathName) throws IOException;

	/**
	 * Returns a list of actions which are necessary to change the target path with regards to content of the source
	 * path.
	 * 
	 * @param sourcePathName The source path.
	 * @param targetPathName The target path.
	 * @return A list list of actions which are necessary to change the target path with regards to content of the
	 *         source path or an empty list if both paths are of equal content.
	 * @throws IOException If something gets wrong while reading the file information.
	 */
	List<MirrorActionSO> buildActionList(String sourcePathName, String targetPathName) throws IOException;

}