package de.ollie.fstools.mirror;

/**
 * A calculator buffer sizes depending of the free memory and a passed source file length.
 *
 * @author ollie (15.10.2020)
 */
public class BufferSizeCalculator {

	private final FreeMemProvider freeMemProvider;

	public BufferSizeCalculator(FreeMemProvider freeMemProvider) {
		super();
		this.freeMemProvider = freeMemProvider;
	}

	/**
	 * Calculates a buffer size for a copy operation respecting the passed file size and the free memory.
	 * 
	 * @param sourceFileLength The length of the source file which the buffer size is to calculate for.
	 * @return The calculated buffer size.
	 */
	public int calc(long sourceFileLength) {
		long free = freeMemProvider.freeMemory();
		free = free > Integer.MAX_VALUE ? Integer.MAX_VALUE : free;
		return (int) (free > sourceFileLength ? sourceFileLength : (int) (free * 0.8));
	}

}