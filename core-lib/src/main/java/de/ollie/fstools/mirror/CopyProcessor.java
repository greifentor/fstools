package de.ollie.fstools.mirror;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import de.ollie.fstools.copier.FileCopier;

/**
 * A class which is able to copy a file.
 *
 * @author ollie (15.10.2020)
 */
public class CopyProcessor {

	private final BufferSizeCalculator bufferSizeCalculator;

	public CopyProcessor(BufferSizeCalculator bufferSizeCalculator) {
		super();
		this.bufferSizeCalculator = bufferSizeCalculator;
	}

	/**
	 * Copies a file dependent by its size. Files smaller than the passed "minFileLengthForCopier" parameter war copied
	 * using the "Files.copy" method. Larger files are copied by the "Copier" class of the "fstools". This allows to
	 * monitor the copy process.
	 * 
	 * @param action                        The mirror action which causes the file copy.
	 * @param mirrorActionProcessorObserver An observer for the copy action which is called only files larger than
	 *                                      "minFileLengthForCopier".
	 * @param minFileLengthForCopier        A minimum file size which causes a use Copier instead of the "Files.copy"
	 *                                      method. This allows to monitor the copy process.
	 */
	public void copyFile(MirrorAction action, MirrorActionProcessorObserver mirrorActionProcessorObserver,
			int minFileLengthForCopier) throws IOException {
		long sourceFileLength = new File(action.getSourceFileName()).length();
		if (sourceFileLength >= minFileLengthForCopier) {
			FileCopier copier = new FileCopier(bufferSizeCalculator.calc(sourceFileLength));
			copier.addFileCopierListener(
					event -> mirrorActionProcessorObserver.partialCopied(MirrorActionProcessorPartialCopyEvent
							.of(action, event.getBytesCopied() + event.getBytesLeft(), event.getBytesLeft())));
			copier.copy(new File(action.getSourceFileName()), new File(action.getTargetFileName()));
		} else {
			Files.copy(Paths.get(action.getSourceFileName()), Paths.get(action.getTargetFileName()),
					StandardCopyOption.REPLACE_EXISTING);
		}
	}

}