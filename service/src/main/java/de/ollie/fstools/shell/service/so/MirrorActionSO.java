package de.ollie.fstools.shell.service.so;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * A container for mirror action service objects.
 *
 * @author ollie (12.09.2020)
 */
@Accessors(chain = true)
@Data
@Generated
public class MirrorActionSO {

	public enum ActionTypeSO {
		COPY, //
		REMOVE;
	}

	private String differenceMessage;
	private String sourceFileName;
	private long sourceFileSizeInBytes;
	private String targetFileName;
	private ActionTypeSO type;

}