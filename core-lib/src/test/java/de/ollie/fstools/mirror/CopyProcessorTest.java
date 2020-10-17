package de.ollie.fstools.mirror;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CopyProcessorTest {

	@Mock
	private BufferSizeCalculator bufferSizeCalculator;

	@InjectMocks
	private CopyProcessor copyProcessor;

	@DisplayName("Tests of method 'copyFile(MirrorAction, MirrorActionProcessorObserver, int)'.")
	@Nested
	class TestsOfMethod_copyFile_MirrorAction_MirrorActionProcessorObserver_int {

		@DisplayName("Copies a file lesser than passed 'minFileLengthForCopier' by using the 'Files.copy' method. No"
				+ "observer events are fired.")
		@Test
		void passingAnActionForAFileWhoseLengthIsLessThanMinFileLengthForCopier_UsingFileCopyMethod_NoObserverEvents() {
			// Prepare

			// Run
			// Check
		}

	}

}