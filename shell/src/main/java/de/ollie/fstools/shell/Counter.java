package de.ollie.fstools.shell;

/**
 * A counter for test purpose.
 *
 * @author ollie (02.01.2020)
 */
public class Counter {

	private int count = 0;

	public Counter inc() {
		count++;
		return this;
	}

	public int getCount() {
		return this.count;
	}

}