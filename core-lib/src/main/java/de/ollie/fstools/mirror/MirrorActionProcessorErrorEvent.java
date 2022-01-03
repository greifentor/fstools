package de.ollie.fstools.mirror;

import de.ollie.fstools.mirror.MirrorAction.ActionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * A container for error events.
 *
 * @author ollie (03.01.2022)
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.PRIVATE)
public class MirrorActionProcessorErrorEvent extends MirrorActionProcessorEvent {

	private Exception exception;

	private MirrorActionProcessorErrorEvent() {
		super();
	}

	public static MirrorActionProcessorErrorEvent of(MirrorAction action, Exception exception) {
		MirrorActionProcessorErrorEvent event = new MirrorActionProcessorErrorEvent().setException(exception);
		event.setSourceFileName(action.getSourceFileName());
		event.setTargetFileName(action.getTargetFileName());
		event.setType(action.getType());
		return event;
	}

}
