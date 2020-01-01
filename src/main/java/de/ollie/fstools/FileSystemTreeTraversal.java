package de.ollie.fstools;

import static de.ollie.utils.Check.ensure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * A class which performs a tree traversal on a file system tree.
 *
 * @author ollie (01.01.2020)
 */
public class FileSystemTreeTraversal {

	private Path workingPath = null;

	/**
	 * Creates a new tree traversal for the passed path.
	 *
	 * @param path The path which the file system tree traversal should work with.
	 */
	public FileSystemTreeTraversal(Path workingPath) {
		super();
		ensure(workingPath != null, "path cannot be null.");
		this.workingPath = workingPath;
	}

	/**
	 * Returns the working path of the traversal.
	 * 
	 * @return The working path of the traversal.
	 */
	public Path getWorkingPath() {
		return this.workingPath;
	}

	/**
	 * Traverses the working tree and fires events in case detecting a file or a directory.
	 * 
	 * @throws FileNotFoundException In case of working tree is not existing.
	 */
	public void traverse() throws IOException {
		// PROCEED HERE !!!.
	}

}