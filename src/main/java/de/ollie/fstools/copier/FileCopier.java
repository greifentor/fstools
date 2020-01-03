package de.ollie.fstools.copier;

import static de.ollie.utils.Check.ensure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class which is able to copy files in a file system.
 *
 * @author ollie (02.01.2020)
 */
public class FileCopier {

	private int bufferSize = 0;
	private List<FileCopierListener> listeners = new ArrayList<>();

	/**
	 * Creates a file copier object with default values.
	 */
	public FileCopier() {
		this(1024 * 1024);
	}

	/**
	 * Creates a file copier object with the passed values.
	 * 
	 * @param bufferSize An alternate buffer size.
	 */
	public FileCopier(int bufferSize) {
		super();
		this.bufferSize = bufferSize;
	}

	/**
	 * Adds the passed listener to the listeners which are observing the file copier.
	 * 
	 * @param listener The listener to add.
	 * @throws IllegalArgumentException Passing a null value.
	 */
	public void addFileCopierListener(FileCopierListener listener) {
		ensure(listener != null, "listener cannot be null.");
		this.listeners.add(listener);
	}

	/**
	 * Copies the source file content to the target file.
	 * 
	 * @param sourceFile The file whose content is to copy to the target file.
	 * @param targetFile The file which the content of the source file is to copy into. If the file is not existing it
	 *                   will be created otherwise it will be overwritten.
	 * @throws IOException              If an error occurs while copying the file content.
	 * @throws FileNotFoundException    If the source file does not exists.
	 * @throws IllegalArgumentException If one or both parameters are passed as null values.
	 */
	public void copy(File sourceFile, File targetFile) throws IOException {
		ensure(sourceFile != null, "source file cannot be null.");
		ensure(targetFile != null, "targetfile cannot be null.");
		ensure(sourceFile.exists(),
				new FileNotFoundException("source file is not existing: " + sourceFile.getAbsolutePath()));
		try ( //
				FileInputStream inputStream = new FileInputStream(sourceFile);
				FileOutputStream outputStream = new FileOutputStream(targetFile) //
		) {
			byte[] fileContent = new byte[this.bufferSize];
			int offset = 0;
			int cnt = 0;
			long bytesLeft = sourceFile.length();
			long bytesCopied = 0;
			while ((cnt = inputStream.read(fileContent, offset, this.bufferSize)) > 0) {
				outputStream.write(fileContent, offset, cnt);
				bytesCopied += cnt;
				bytesLeft -= cnt;
				fireFileCopierEvent(sourceFile, targetFile, bytesCopied, bytesLeft);
			}
		}
	}

	protected void fireFileCopierEvent(File sourceFile, File targetFile, long bytesCopied, long bytesLeft) {
		this.listeners.forEach(listener -> {
			try {
				listener.fileCopyStepDetected(new FileCopierEvent() //
						.setAbsoluteSourcePathName(sourceFile.getAbsolutePath()) //
						.setAbsoluteTargetPathName(targetFile.getAbsolutePath()) //
						.setBytesCopied(bytesCopied) //
						.setBytesLeft(bytesLeft) //
				);
			} catch (Exception e) {
				throw new RuntimeException("error occured while calling file found listener: " + e.getMessage() // NOSONAR
						+ ", exception type: " + e.getClass().getName());
			}
		});
	}

}