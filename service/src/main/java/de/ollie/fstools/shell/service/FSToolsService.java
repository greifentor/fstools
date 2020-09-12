package de.ollie.fstools.shell.service;

import java.io.IOException;

import de.ollie.fstools.filestats.FileStats;

/**
 * An interface for the fstools service.
 *
 * @author ollie (11.09.2020)
 */
public interface FSToolsService {

	/**
	 * Returns the information for the file with the passed path.
	 * 
	 * @param path The path of the file whose information are to return.
	 * @return The information of the file whose path has been passed.
	 * @throws IOException In case of an error occurs while reading the file stats.
	 */
	FileStats getFileStats(String path) throws IOException;

}