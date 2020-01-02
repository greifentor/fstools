package de.ollie.fstools.treestats;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * A container for statistic information about a file system tree.
 *
 * @author ollie (02.01.2020)
 */
@Accessors(chain = true)
@Data
@Generated
public class TreeStats {

	private long fileCount;
	private long folderCount;
	private long size;

}