package de.ollie.fstools.shell.service;

import de.ollie.fstools.mirror.MirrorAction.ActionType;
import de.ollie.fstools.mirror.MirrorActionProcessorEvent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * A container for events of processing of mirror actions.
 *
 * @author ollie (16.09.2020)
 */
@Accessors(chain = true)
@Data
@Setter(AccessLevel.PRIVATE)
public class ProcessMirrorActionsEvent {

	private String sourceFileName;
	private String targetFileName;
	private ActionType type;

	private ProcessMirrorActionsEvent() {
		super();
	}

	public static ProcessMirrorActionsEvent of(MirrorActionProcessorEvent event) {
		return new ProcessMirrorActionsEvent() //
				.setSourceFileName(event.getSourceFileName()) //
				.setTargetFileName(event.getTargetFileName()) //
				.setType(event.getType()) //
		;
	}

}