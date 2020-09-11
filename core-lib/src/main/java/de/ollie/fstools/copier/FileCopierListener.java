package de.ollie.fstools.copier;

/**
 * A listener for file copier events.
 *
 * @author ollie (03.01.2020)
 */
@FunctionalInterface
public interface FileCopierListener {

	/**
	 * Fires an event if a copy step is finished.
	 * 
	 * @param event The data of the event.
	 */
	void fileCopyStepDetected(FileCopierEvent event);

}