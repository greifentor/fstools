package de.ollie.fstools.filestats;

import static de.ollie.utils.Check.ensure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import org.springframework.stereotype.Component;

import de.ollie.fstools.filestats.FileStats.FileType;

/**
 * A reader for file stats.
 * 
 * @author Oliver.Lieshoff (06.09.2020)
 *
 */
@Component
public class FileStatsReader {

	/**
	 * Reads the stats for the file with the passed name.
	 * 
	 * @param fileName The name of the file whose stats are to read.
	 * @return A file stats container for the file with the passed name.
	 * @throws IOException In case of an error occurs while reading the file stats.
	 */
	public FileStats read(String fileName) throws IOException {
		ensure(fileName != null, new NullPointerException("file name cannot be null."));
		Path path = Paths.get(fileName);
		Map<String, Object> stats = Files.readAttributes(path, "*", LinkOption.NOFOLLOW_LINKS);
		return new FileStats() //
				.setLastModifiedTime(fromFileTime((FileTime) stats.get("lastModifiedTime"))) //
				.setName(path.toString().replace("\\", "/")) //
				.setSize((Long) stats.get("size")) //
				.setType(getType(stats)) //
		;
	}

	private LocalDateTime fromFileTime(FileTime fileTime) {
		Instant instant = Instant.ofEpochMilli((fileTime.toMillis() / 1000) * 1000);
		return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	private FileType getType(Map<String, Object> stats) {
		Boolean isDirectory = (Boolean) stats.get("isDirectory");
		Boolean isRegularFile = (Boolean) stats.get("isRegularFile");
		if (Boolean.TRUE.equals(isDirectory)) {
			return FileType.DIRECTORY;
		} else if (Boolean.TRUE.equals(isRegularFile)) {
			return FileType.FILE;
		}
		return FileType.OTHER;
	}

	public static void main(String[] args) {
		FileStatsReader reader = new FileStatsReader();
		for (String arg : args) {
			try {
				System.out.println(reader.read(arg));
			} catch (Exception e) {
				System.out.println(arg + " -> ERROR: " + e.getMessage());
			}
		}
	}

}