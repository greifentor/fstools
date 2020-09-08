package de.ollie.fstools.mirror;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * A container for mirror actions.
 * 
 * @author Oliver.Lieshoff (06.09.2020)
 *
 */
@Accessors(chain = true)
@Data
@Generated
public class MirrorAction {

	public enum ActionType {
		COPY, //
		REMOVE
	}

	private String sourceFileName;
	private String targetFileName;
	private ActionType type;

	public String getSourceFileName() {
		return sourceFileName != null ? sourceFileName.replace("\\", "/") : null;
	}

	public String getTargetFileName() {
		return targetFileName != null ? targetFileName.replace("\\", "/") : null;
	}

}