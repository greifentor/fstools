package de.ollie.fstools.treestats;

import static de.ollie.utils.Check.ensure;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.ollie.fstools.traversal.FileSystemTreeTraversal;

/**
 * A class which computes some statistic information for a specific path.
 *
 * @author ollie (02.01.2020)
 */
public class TreeStatCalculator {

	static PrintStream out = System.out; // NOSONAR OLI So it have to be.

	/**
	 * Starts the tree statistic calculator from a main method.
	 * 
	 * @param args The command line parameters of the program call.
	 * @throws IOException In case of an error occurs while calculating the statistic information.
	 */
	public static void main(String[] args) throws Exception {
		ensure(args != null, "arguments cannot be null.");
		ensure(args.length > 0, "not enough arguments.");
		Path path = Paths.get(args[0]);
		TreeStats stats = new TreeStatCalculator().calculate(path);
		out.println();
		out.println("Statistic information for: " + path.toString().replace("\\", "/"));
		out.println("Folders: " + stats.getFolderCount());
		out.println("Files:   " + stats.getFileCount());
		out.println("Size:    " + stats.getSize());
		out.println();
	}

	/**
	 * Computes the statistic information for the passed path.
	 * 
	 * @param path The path which the statistic information should be computed for.
	 * @return An object with the statistic information for the passed path.
	 * @throws IOException In case of having an error while calculating the result.
	 */
	public TreeStats calculate(Path path) throws IOException {
		ensure(path != null, "path to calculate statistic information for cannot be null.");
		TreeStats result = new TreeStats();
		FileSystemTreeTraversal traversal = new FileSystemTreeTraversal(path);
		traversal.addDirectoryFoundListener(event -> result.setFolderCount(result.getFolderCount() + 1));
		traversal.addFileFoundListener(event -> result.setFileCount(result.getFileCount() + 1)
				.setSize(result.getSize() + event.getPath().toFile().length()));
		traversal.traverse();
		return result;
	}

}