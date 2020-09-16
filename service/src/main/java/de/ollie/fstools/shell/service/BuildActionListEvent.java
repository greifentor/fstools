package de.ollie.fstools.shell.service;

import de.ollie.fstools.filestats.FileStats;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * A container for build action list events.
 *
 * @author ollie (16.09.2020)
 */
@Accessors(chain = true)
@Data
@Setter(AccessLevel.PRIVATE)
public class BuildActionListEvent {

	private FileStats fileStats;

	private BuildActionListEvent() {
		super();
	}

	public static BuildActionListEvent of(FileStats fileStats) {
		return new BuildActionListEvent() //
				.setFileStats(fileStats) //
		;
	}

}