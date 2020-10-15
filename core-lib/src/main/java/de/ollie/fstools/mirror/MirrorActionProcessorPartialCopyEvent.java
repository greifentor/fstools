package de.ollie.fstools.mirror;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * A container for partial copy events.
 *
 * @author ollie (14.10.2020)
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.PRIVATE)
public class MirrorActionProcessorPartialCopyEvent extends MirrorActionProcessorEvent {

	private long bytesTotal;
	private long bytesLeft;

	private MirrorActionProcessorPartialCopyEvent() {
		super();
	}

	public static MirrorActionProcessorPartialCopyEvent of(MirrorAction action, long bytesTotal, long bytesLeft) {
		MirrorActionProcessorPartialCopyEvent event = new MirrorActionProcessorPartialCopyEvent() //
				.setBytesLeft(bytesLeft) //
				.setBytesTotal(bytesTotal) //
		;
		event.setSourceFileName(action.getSourceFileName());
		event.setTargetFileName(action.getTargetFileName());
		event.setType(action.getType());
		return event;
	}

}