package de.ollie.fstools.filestats;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * A container for file stats.
 * 
 * @author Oliver.Lieshoff (06.09.2020)
 *
 */
@Accessors(chain = true)
@Data
@Generated
public class FileStats {

	public enum FileType {
		DIRECTORY, //
		FILE, //
		OTHER
	}

	private LocalDateTime lastModifiedTime;
	private String name;
	private long size;
	private FileType type;

	public String getName() {
		return name != null ? name.replace("\\", "/") : null;
	}

}