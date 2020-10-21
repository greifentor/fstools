package de.ollie.fstools.touch;

import static de.ollie.utils.Check.ensure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * A class which implements methods similar to the UNIX touch command.
 *
 * @author ollie (19.10.2020)
 */
public class Touch {

	/**
	 * Sets the last access time to the passed timestamps.
	 * 
	 * @param fileName         The name of the file whose dates are to modify.
	 * @param lastModifiedTime The timestamp which is to set as last modify time.
	 * @throws NullPointerException Passing a null value as file name or last access time.
	 * @throws NoSuchFileException  If no file exists for the passed file name.
	 */
	public void touch(String fileName, LocalDateTime lastModifiedTime) throws IOException {
		ensure(fileName != null, new NullPointerException("file name cannot be null."));
		ensure(lastModifiedTime != null, new NullPointerException("last modified time cannot be null."));
		File file = new File(fileName);
		ensure(file.exists(), new NoSuchFileException(fileName, null, "file '" + fileName + "' does not exists."));
		Path path = Paths.get(fileName);
		Files.setLastModifiedTime(path, FileTime.fromMillis(millisFromLocalDateTime(lastModifiedTime)));
	}

	private long millisFromLocalDateTime(LocalDateTime localDateTime) {
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

}