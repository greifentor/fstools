package de.ollie.fstools.mirror;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

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
	 * @throws IOException If an error occurs while processing the mirror actions.
	 */
	public void processMirrorActions(List<MirrorAction> actions,
			MirrorActionProcessorObserver mirrorActionProcessorObserver) throws IOException {
		for (MirrorAction action : actions) {
			if (action.getType() == ActionType.COPY) {
				createFolderIfNecessary(action);
				mirrorActionProcessorObserver.copying(MirrorActionProcessorEvent.of(action));
				Files.copy(Paths.get(action.getSourceFileName()), Paths.get(action.getTargetFileName()),
						StandardCopyOption.REPLACE_EXISTING);
				mirrorActionProcessorObserver.copied(MirrorActionProcessorEvent.of(action));
			} else if (action.getType() == ActionType.REMOVE) {
				mirrorActionProcessorObserver.removing(MirrorActionProcessorEvent.of(action));
				Files.walk(Paths.get(action.getTargetFileName())) //
						.sorted(Comparator.reverseOrder()) //
						.map(Path::toFile) //
						.forEach(File::delete) //
				;
				// Files.delete(Paths.get(action.getTargetFileName()));
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

}