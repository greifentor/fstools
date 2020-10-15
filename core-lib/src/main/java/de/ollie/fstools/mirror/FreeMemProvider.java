package de.ollie.fstools.mirror;

/**
 * A provider for the current free memory of the JVM.
 *
 * @author ollie (15.10.2020)
 */
public class FreeMemProvider {

	public long freeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

}