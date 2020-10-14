package de.ollie.fstools.mirror;

/**
 * An interface for mirror action processor observers.
 *
 * @author ollie (16.09.2020)
 */
public interface MirrorActionProcessorObserver {

	/**
	 * Called if the mirror action processor is going to copy a file.
	 * 
	 * @param event The necessary data of the copy action.
	 */
	void copying(MirrorActionProcessorEvent event);

	/**
	 * Called if the mirror action processor has copied a file.
	 * 
	 * @param event The necessary data of the copy action.
	 */
	void copied(MirrorActionProcessorEvent event);

	/**
	 * Called if the mirror action processor has copied a part of a file.
	 * 
	 * @param event The necessary data of the partial copy.
	 */
	void partialCopied(MirrorActionProcessorPartialCopyEvent event);

	/**
	 * Called if the mirror action processor is going to remove a file.
	 * 
	 * @param event The necessary data of the remove action.
	 */
	void removing(MirrorActionProcessorEvent event);

	/**
	 * Called if the mirror action processor has removed a file.
	 * 
	 * @param event The necessary data of the remove action.
	 */
	void removed(MirrorActionProcessorEvent event);

}