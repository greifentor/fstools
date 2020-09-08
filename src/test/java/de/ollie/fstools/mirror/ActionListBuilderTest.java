package de.ollie.fstools.mirror;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.fstools.mirror.MirrorAction.ActionType;

@ExtendWith(MockitoExtension.class)
public class ActionListBuilderTest {

	private static final String PREFIX = "src/test/resources/mirrortest/";
	private static final String SOURCE_FOLDER = "/sourcefolder";
	private static final String TARGET_FOLDER = "/targetfolder";

	@InjectMocks
	private ActionListBuilder unitUnderTest;

	@DisplayName("Tests for method 'build(String, String)'.")
	@Nested
	class TestsForMethod_build_String_String {

		@DisplayName("Throws an exception if a null value as source folder name is passed.")
		@Test
		void nullAsSourceFolderNamePassed_ThrowsAnException() {
			assertThrows(NullPointerException.class, () -> unitUnderTest.build(null, "src"));
		}

		@DisplayName("Throws an exception if a null value as target folder name is passed.")
		@Test
		void nullAsTargetFolderNamePassed_ThrowsAnException() {
			assertThrows(NullPointerException.class, () -> unitUnderTest.build("src", null));
		}

		@DisplayName("Returns an empty list for equal folders.")
		@Test
		void calledForEqualFolders_ReturnsAnEmptyList(@TempDir Path tempDir) throws Exception {
			// Prepare
			Path sourceFile = tempDir.resolve("source/afile.txt");
			Files.write(sourceFile, "Some characters as file content.".getBytes());
			Path targetFile = tempDir.resolve("target/afile.txt");
			Files.write(targetFile, "Some characters as file content.".getBytes());
			List<MirrorAction> expected = new ArrayList<>();
			// Run
			System.out.println(new File(sourceFile.toString()).getCanonicalPath());
			System.out.println(sourceFile.toAbsolutePath());
			System.out.println(new File(targetFile.toString()).getCanonicalPath());
			System.out.println(targetFile.toAbsolutePath());
			List<MirrorAction> returned = unitUnderTest.build(sourceFile.toString(), targetFile.toString());
			// Check
			assertEquals(expected, returned);
		}

		@DisplayName("Returns a list of actions for a source folders with an additional file.")
		@Test
		void calledForASourceFolderWhichContainsAnAdditionalFile_ReturnsAListOfActions() throws Exception {
			// Prepare
			String folderName = "folderWithAdditionalFile";
			List<MirrorAction> expected = Arrays.asList( //
					new MirrorAction() //
							.setSourceFileName(PREFIX + folderName + SOURCE_FOLDER + "/anotherfile.txt") //
							.setTargetFileName(PREFIX + folderName + TARGET_FOLDER + "/anotherfile.txt") //
							.setType(ActionType.COPY) //
			);
			// Run
			List<MirrorAction> returned = unitUnderTest.build(PREFIX + folderName + SOURCE_FOLDER,
					PREFIX + folderName + TARGET_FOLDER);
			// Check
			assertEquals(expected, returned);
		}

		@DisplayName("Returns a list of actions for a source folders with a different file (size).")
		@Test
		void calledForASourceFolderWhichContainsADifferentFileSize_ReturnsAListOfActions() throws Exception {
			// Prepare
			String folderName = "folderWithDifferentFileSize";
			List<MirrorAction> expected = Arrays.asList( //
					new MirrorAction() //
							.setSourceFileName(PREFIX + folderName + SOURCE_FOLDER + "/differentfile-size.txt") //
							.setTargetFileName(PREFIX + folderName + TARGET_FOLDER + "/differentfile-size.txt") //
							.setType(ActionType.COPY) //
			);
			// Run
			List<MirrorAction> returned = unitUnderTest.build(PREFIX + folderName + SOURCE_FOLDER,
					PREFIX + folderName + TARGET_FOLDER);
			// Check
			assertEquals(expected, returned);
		}

	}

}