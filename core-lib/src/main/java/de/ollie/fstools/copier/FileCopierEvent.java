package de.ollie.fstools.copier;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * A container for file copier event data.
 *
 * @author ollie (03.01.2020)
 */
@Accessors(chain = true)
@Data
@Generated
public class FileCopierEvent {

	private String absoluteSourcePathName;
	private String absoluteTargetPathName;
	private long bytesCopied;
	private long bytesLeft;

}