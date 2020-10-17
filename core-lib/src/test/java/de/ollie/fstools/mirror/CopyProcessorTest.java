package de.ollie.fstools.mirror;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.fstools.mirror.MirrorAction.ActionType;
import de.ollie.fstools.mirror.MirrorAction.DifferenceType;

@ExtendWith(MockitoExtension.class)
public class CopyProcessorTest {

	@Mock
	private BufferSizeCalculator bufferSizeCalculator;
	@Mock
	private MirrorActionProcessorObserver observer;

	@InjectMocks
	private CopyProcessor unitUnderTest;

	@DisplayName("Tests of method 'copyFile(MirrorAction, MirrorActionProcessorObserver, int)'.")
	@Nested
	class TestsOfMethod_copyFile_MirrorAction_MirrorActionProcessorObserver_int {

		@DisplayName("Copies a file lesser than passed 'minFileLengthForCopier' by using the 'Files.copy' method. No"
				+ "observer events are fired.")
		@Test
		void passingAnActionForAFileWhoseLengthIsLessThanMinFileLengthForCopier_UsingFileCopyMethod_NoObserverEvents(
				@TempDir Path tempDir) throws Exception {
			// Prepare
			Files.createDirectory(Paths.get(tempDir.toString(), "/source"));
			Files.createDirectory(Paths.get(tempDir.toString(), "/target"));
			String sourceContent = "12345678901234567890";
			File f = new File(tempDir + "/source/testfile.txt");
			Files.write(Paths.get(f.getAbsolutePath()), sourceContent.getBytes());
			MirrorAction action = new MirrorAction() //
					.setDifferenceType(DifferenceType.TIME) //
					.setSourceFileName(tempDir.toString() + "/source/testfile.txt") //
					.setSourceFileSizeInBytes(f.length()) //
					.setTargetFileName(tempDir.toString() + "/target/testfile.txt") //
					.setType(ActionType.COPY) //
			;
			int minFileLengthFroCopier = sourceContent.length() * 2;
			// Run
			unitUnderTest.copyFile(action, observer, minFileLengthFroCopier);
			// Check
			String targetContent = new String(
					Files.readAllBytes(Paths.get(tempDir.toString() + "/target/testfile.txt")));
			assertEquals(sourceContent, targetContent);
			verifyNoInteractions(observer);
		}

		@DisplayName("Copies a file lesser than passed 'minFileLengthForCopier' by using the 'Files.copy' method. No"
				+ "observer events are fired.")
		@Test
		void passingAnActionForAFileWhoseLengthIsGreaterThanMinFileLengthForCopier_UsingFileCopyMethod_OneObserverEvent(
				@TempDir Path tempDir) throws Exception {
			// Prepare
			Files.createDirectory(Paths.get(tempDir.toString(), "/source"));
			Files.createDirectory(Paths.get(tempDir.toString(), "/target"));
			String sourceContent = "12345678901234567890";
			File f = new File(tempDir + "/source/testfile.txt");
			Files.write(Paths.get(f.getAbsolutePath()), sourceContent.getBytes());
			MirrorAction action = new MirrorAction() //
					.setDifferenceType(DifferenceType.TIME) //
					.setSourceFileName(tempDir.toString() + "/source/testfile.txt") //
					.setSourceFileSizeInBytes(f.length()) //
					.setTargetFileName(tempDir.toString() + "/target/testfile.txt") //
					.setType(ActionType.COPY) //
			;
			int minFileLengthForCopier = sourceContent.length() / 2;
			when(bufferSizeCalculator.calc(anyLong())).thenReturn(minFileLengthForCopier);
			// Run
			unitUnderTest.copyFile(action, observer, minFileLengthForCopier);
			// Check
			String targetContent = new String(
					Files.readAllBytes(Paths.get(tempDir.toString() + "/target/testfile.txt")));
			assertEquals(sourceContent, targetContent);
			verify(observer, times(1)).partialCopied(MirrorActionProcessorPartialCopyEvent.of(action, 20, 10));
			verify(observer, times(1)).partialCopied(MirrorActionProcessorPartialCopyEvent.of(action, 20, 0));
			verifyNoMoreInteractions(observer);
		}

	}

}