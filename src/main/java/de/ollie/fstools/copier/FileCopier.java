package de.ollie.fstools.copier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A class which is able to copy files in a file system.
 *
 * @author ollie (02.01.2020)
 */
public class FileCopier {

	/**
	 * Copies the source file content to the target file.
	 * 
	 * @param sourceFile The file whose content is to copy to the target file.
	 * @param targetFile The file which the content of the source file is to copy into. If the file is not existing it
	 *                   will be created otherwise it will be overwritten.
	 * @throws IOException           If an error occurs while copying the file content.
	 * @throws FileNotFoundException If the source file does not exists.
	 */
	public void copyFile(File sourceFile, File targeFile) throws IOException {
	}

}