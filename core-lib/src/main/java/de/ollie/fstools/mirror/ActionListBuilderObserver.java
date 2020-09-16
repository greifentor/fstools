package de.ollie.fstools.mirror;

/**
 * Interface for objects which should be able to observe build processes.
 *
 * @author ollie (16.09.2020)
 */
public interface ActionListBuilderObserver {

	/**
	 * Is called if a new file is detected to get explored for the action list.
	 * 
	 * @param event A container with the data of the event.
	 */
	void fileDetected(ActionListBuilderEvent event);

	/**
	 * Is called if a new folder is detected to get explored for the action list.
	 * 
	 * @param event A container with the data of the event.
	 */
	void folderDetected(ActionListBuilderEvent event);

}