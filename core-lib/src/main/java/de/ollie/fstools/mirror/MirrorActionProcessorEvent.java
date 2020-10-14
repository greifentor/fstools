package de.ollie.fstools.mirror;

import de.ollie.fstools.mirror.MirrorAction.ActionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * A container for mirror action processor events.
 *
 * @author ollie (16.09.2020)
 */
@Accessors(chain = true)
@Data
@Setter(AccessLevel.PROTECTED)
public class MirrorActionProcessorEvent {

	private String sourceFileName;
	private String targetFileName;
	private ActionType type;

	protected MirrorActionProcessorEvent() {
		super();
	}

	public static MirrorActionProcessorEvent of(MirrorAction action) {
		return new MirrorActionProcessorEvent() //
				.setSourceFileName(action.getSourceFileName()) //
				.setTargetFileName(action.getTargetFileName()) //
				.setType(action.getType()) //
		;
	}

}