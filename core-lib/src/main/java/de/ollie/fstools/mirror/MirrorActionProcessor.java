package de.ollie.fstools.mirror;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import de.ollie.fstools.copier.FileCopier;
import de.ollie.fstools.mirror.MirrorAction.ActionType;

/**
 * A processor for mirror actions.
 *
 * @author ollie (15.09.2020)
 */
@Component
public class MirrorActionProcessor {

	/**
	 * Processes the passed mirror actions.
	 * 
	 * @param mirrorActions                 The mirror actions to process.
	 * @param mirrorActionProcessorObserver An object which observes the mirror action processing.
	 * @param minFileLengthForCopier        An amount in bytes which a file must have in minimum to change to Copier zu
	 *                                      copy.
	 * @throws IOException If an error occurs while processing the mirror actions.
	 */
	public void processMirrorActions(List<MirrorAction> actions,
			MirrorActionProcessorObserver mirrorActionProcessorObserver, int minFileLengthForCopier)
			throws IOException {
		for (MirrorAction action : actions) {
			if (action.getType() == ActionType.COPY) {
				createFolderIfNecessary(action);
				mirrorActionProcessorObserver.copying(MirrorActionProcessorEvent.of(action));
				copyFile(action, mirrorActionProcessorObserver, minFileLengthForCopier);
				mirrorActionProcessorObserver.copied(MirrorActionProcessorEvent.of(action));
			} else if (action.getType() == ActionType.REMOVE) {
				mirrorActionProcessorObserver.removing(MirrorActionProcessorEvent.of(action));
				Stream<Path> pathes = Files.walk(Paths.get(action.getTargetFileName()));
				try {
					pathes //
							.sorted(Comparator.reverseOrder()) //
							.map(Path::toFile) //
							.forEach(File::delete) //
					;
				} finally {
					pathes.close();
				}
				mirrorActionProcessorObserver.removed(MirrorActionProcessorEvent.of(action));
			}
		}
	}

	private void createFolderIfNecessary(MirrorAction action) throws IOException {
		int i = action.getTargetFileName().lastIndexOf('/');
		if (i > -1) {
			String folderName = action.getTargetFileName().substring(0, i);
			File folder = new File(folderName);
			if (!folder.exists()) {
				folder.mkdirs();
			}
		}
	}

	private void copyFile(MirrorAction action, MirrorActionProcessorObserver mirrorActionProcessorObserver,
			int minFileLengthForCopier) throws IOException {
		long sourceFileLength = new File(action.getSourceFileName()).length();
//		if (sourceFileLength >= minFileLengthForCopier) {
		FileCopier copier = new FileCopier(computeBufferSize(sourceFileLength));
		copier
				.addFileCopierListener(
						event -> mirrorActionProcessorObserver
								.partialCopied(
										MirrorActionProcessorPartialCopyEvent
												.of(
														action,
														event.getBytesCopied() + event.getBytesLeft(),
														event.getBytesLeft())));
		copier.copy(new File(action.getSourceFileName()), new File(action.getTargetFileName()));
//		} else {
//			Files.copy(Paths.get(action.getSourceFileName()), Paths.get(action.getTargetFileName()),
//					StandardCopyOption.REPLACE_EXISTING);
//	}
	}

	private int computeBufferSize(long sourceFileLength) {
		long free = Runtime.getRuntime().freeMemory();
		free = free > Integer.MAX_VALUE ? Integer.MAX_VALUE : free;
		return (int) (free > sourceFileLength ? sourceFileLength : (int) (free * 0.8));
	}

}