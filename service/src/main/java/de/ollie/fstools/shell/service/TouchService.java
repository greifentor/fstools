package de.ollie.fstools.shell.service;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;

/**
 * A service for touch operations.
 *
 * @author ollie (20.10.2020)
 */
public interface TouchService {

	/**
	 * Sets the last access time to the passed timestamps.
	 * 
	 * @param fileName         The name of the file whose dates are to modify.
	 * @param lastModifiedTime The timestamp which is to set as last modify time.
	 * @throws NullPointerException Passing a null value as file name or last access time.
	 * @throws NoSuchFileException  If no file exists for the passed file name.
	 */
	void touch(String fileName, LocalDateTime lastModifiedTime) throws IOException;

}