package de.ollie.fstools.mirror;

import de.ollie.fstools.filestats.FileStats;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * A container for action list builder events.
 *
 * @author ollie (16.09.2020)
 */
@Accessors(chain = true)
@Data
@Setter(AccessLevel.PRIVATE)
public class ActionListBuilderEvent {

	private FileStats fileStats;

	private ActionListBuilderEvent() {
		super();
	}

	public static ActionListBuilderEvent of(FileStats fileStats) {
		return new ActionListBuilderEvent() //
				.setFileStats(fileStats) //
		;
	}

}