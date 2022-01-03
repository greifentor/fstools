package androidrenamer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import de.ollie.fstools.traversal.DirectoryFoundEvent;
import de.ollie.fstools.traversal.DirectoryFoundListener;
import de.ollie.fstools.traversal.FileFoundEvent;
import de.ollie.fstools.traversal.FileFoundListener;
import de.ollie.fstools.traversal.FileSystemTreeTraversal;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A tool which renames files to an Android matching pattern:
 * 
 * <OL>
 * <LI>":" to "-"</LI>
 * <LI>"?" to ""</LI>
 * <LI>""" to "'"</LI>
 * </OL>
 *
 * @author ollie (24.09.2021)
 */
public class AndroidRenamer implements DirectoryFoundListener, FileFoundListener {

	private Map<Path, Path> dirChanges = new HashMap<>();
	private Stack<Path> changedDirs = new Stack<>();

	public static void main(String[] args) throws Exception {
		FileSystemTreeTraversal traversal = new FileSystemTreeTraversal(Path.of(args[0]));
		AndroidRenamer renamer = new AndroidRenamer();
		traversal.addDirectoryFoundListener(renamer);
		traversal.addFileFoundListener(renamer);
		traversal.traverse();
		while (renamer.hasChangedDirs()) {
			renamer.renameNext();
		}
	}

	private boolean hasChangedDirs() {
		return !changedDirs.isEmpty();
	}

	private void renameNext() {
		Path oldPath = changedDirs.pop();
		Path newPath = dirChanges.get(oldPath);
		try {
			Files.move(oldPath, newPath);
		} catch (IOException e) {
			System.out.println("ERROR while renaming dir " + oldPath + " -> " + newPath);
		}
	}

	@AllArgsConstructor
	@Data
	private class Mapping {
		String key;
		String replacement;
	}

	private List<Mapping> mappings = List
			.of(
					new Mapping(" - .", "."),
					new Mapping("?", ""),
					new Mapping(": ", " - "),
					new Mapping(":", " - "),
					new Mapping("\"", "'"),
					new Mapping("Ä", "Ae"),
					new Mapping("ä", "ae"),
					new Mapping("Ö", "Oe"),
					new Mapping("ö", "oe"),
					new Mapping("Ü", "Ue"),
					new Mapping("ü", "ue"),
					new Mapping("ß", "ss"),
					new Mapping("‐", "-"),
					new Mapping("–", "-"),
					new Mapping("À", "A"),
					new Mapping("É", "E"),
					new Mapping("è", "e"),
					new Mapping("é", "e"),
					new Mapping("ó", "o"),
					new Mapping("’", "'"));

	@Override
	public void fileFound(FileFoundEvent event) {
		String fileName = event.getPath().getFileName().toString();
		String pathName = event.getPath().toString();
		pathName = event.getPath().toString().substring(0, pathName.length() - fileName.length());
		if (containsOneOf(fileName, mappings)) {
			for (Mapping mapping : mappings) {
				fileName = fileName.replace(mapping.getKey(), mapping.getReplacement());
			}
			Path newPath = Path.of(pathName, fileName);
			System.out.println("file: " + event.getPath() + " -> " + newPath);
			try {
				Files.move(event.getPath(), newPath);
			} catch (IOException e) {
				System.out.println("ERROR while renaming file " + event.getPath() + " -> " + newPath);
			}
		}
	}

	private boolean containsOneOf(String s, List<Mapping> mappings) {
		for (Mapping mapping : mappings) {
			if (s.contains(mapping.getKey())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void directoryFound(DirectoryFoundEvent event) {
		String fileName = event.getPath().getFileName().toString();
		String pathName = event.getPath().toString();
		pathName = event.getPath().toString().substring(0, pathName.length() - fileName.length());
		if (containsOneOf(fileName, mappings)) {
			for (Mapping mapping : mappings) {
				fileName = fileName.replace(mapping.getKey(), mapping.getReplacement());
			}
			Path newPath = Path.of(pathName, fileName);
			System.out.println("dir: " + event.getPath() + " -> " + newPath);
			changedDirs.push(event.getPath());
			dirChanges.put(event.getPath(), newPath);
		}
	}

}