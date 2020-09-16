package de.ollie.fstools.shell.service;

/**
 * An interface for process mirror action observing.
 *
 * @author ollie (16.09.2020)
 */
public interface ProcessMirrorActionsObserver {

	/**
	 * Called if the mirror action processor is going to copy a file.
	 * 
	 * @param event The necessary data of the copy action.
	 */
	void copying(ProcessMirrorActionsEvent event);

	/**
	 * Called if the mirror action processor has copied a file.
	 * 
	 * @param event The necessary data of the copy action.
	 */
	void copied(ProcessMirrorActionsEvent event);

	/**
	 * Called if the mirror action processor is going to remove a file.
	 * 
	 * @param event The necessary data of the remove action.
	 */
	void removing(ProcessMirrorActionsEvent event);

	/**
	 * Called if the mirror action processor has removed a file.
	 * 
	 * @param event The necessary data of the remove action.
	 */
	void removed(ProcessMirrorActionsEvent event);

}