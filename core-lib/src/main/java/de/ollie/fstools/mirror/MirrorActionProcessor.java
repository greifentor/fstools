package de.ollie.fstools.mirror;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
	 * @param mirrorActions The mirror actions to process.
	 * @throws IOException If an error occurs while processing the mirror actions.
	 */
	public void processMirrorActions(List<MirrorAction> actions) throws IOException {
		for (MirrorAction action : actions) {
			if (action.getType() == ActionType.COPY) {
				createFolderIfNecessary(action);
				System.out.print("copying file " + action.getSourceFileName() + " to " + action.getTargetFileName());
				Files.copy(Paths.get(action.getSourceFileName()), Paths.get(action.getTargetFileName()),
						StandardCopyOption.REPLACE_EXISTING);
				System.out.println(" ok");
			} else if (action.getType() == ActionType.REMOVE) {
				System.out.print("deleting file " + action.getTargetFileName());
				Files.delete(Paths.get(action.getTargetFileName()));
				System.out.println(" ok");
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