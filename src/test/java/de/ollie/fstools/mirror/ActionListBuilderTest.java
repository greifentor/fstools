package de.ollie.fstools.mirror;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.fstools.mirror.MirrorAction.ActionType;
import de.ollie.fstools.mirror.MirrorAction.DifferenceType;

@ExtendWith(MockitoExtension.class)
public class ActionListBuilderTest {

	private static final Long DATE = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
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
		void calledForEqualFolders_ReturnsAnEmptyList() throws Exception {
			// Prepare
			String folderName = "equalfolders";
			new File(PREFIX + folderName + SOURCE_FOLDER + "/afile.txt").setLastModified(DATE);
			new File(PREFIX + folderName + TARGET_FOLDER + "/afile.txt").setLastModified(DATE);
			Path sourceFile = Paths.get(PREFIX + folderName + SOURCE_FOLDER);
			Path targetFile = Paths.get(PREFIX + folderName + TARGET_FOLDER);
			List<MirrorAction> expected = new ArrayList<>();
			// Run
			List<MirrorAction> returned = unitUnderTest.build(sourceFile.toString(), targetFile.toString());
			// Check
			assertEquals(expected, returned);
		}

		@DisplayName("Returns a list of actions for a source folders with an additional file.")
		@Test
		void calledForASourceFolderWhichContainsAnAdditionalFile_ReturnsAListOfActions() throws Exception {
			// Prepare
			String folderName = "folderWithAdditionalFile";
			new File(PREFIX + folderName + SOURCE_FOLDER + "/afile.txt").setLastModified(DATE);
			new File(PREFIX + folderName + TARGET_FOLDER + "/afile.txt").setLastModified(DATE);
			List<MirrorAction> expected = Arrays.asList( //
					new MirrorAction() //
							.setDifferenceType(DifferenceType.EXISTENCE) //
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
			new File(PREFIX + folderName + SOURCE_FOLDER + "/afile.txt").setLastModified(DATE);
			new File(PREFIX + folderName + TARGET_FOLDER + "/afile.txt").setLastModified(DATE);
			new File(PREFIX + folderName + SOURCE_FOLDER + "/differentfile-time.txt").setLastModified(DATE);
			new File(PREFIX + folderName + TARGET_FOLDER + "/differentfile-time.txt").setLastModified(DATE);
			List<MirrorAction> expected = Arrays.asList( //
					new MirrorAction() //
							.setDifferenceType(DifferenceType.SIZE) //
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

		@DisplayName("Returns a list of actions for a source folders with a file with new modification date (time).")
		@Test
		void calledForASourceFolderWhichContainsADifferentNewerFileTime_ReturnsAListOfActions() throws Exception {
			// Prepare
			String folderName = "folderWithDifferentFileTime";
			new File(PREFIX + folderName + SOURCE_FOLDER + "/afile.txt").setLastModified(DATE);
			new File(PREFIX + folderName + TARGET_FOLDER + "/afile.txt").setLastModified(DATE);
			new File(PREFIX + folderName + SOURCE_FOLDER + "/differentfile-time.txt").setLastModified(DATE);
			new File(PREFIX + folderName + TARGET_FOLDER + "/differentfile-time.txt").setLastModified(DATE - 1000);
			List<MirrorAction> expected = Arrays.asList( //
					new MirrorAction() //
							.setDifferenceType(DifferenceType.TIME) //
							.setSourceFileName(PREFIX + folderName + SOURCE_FOLDER + "/differentfile-time.txt") //
							.setTargetFileName(PREFIX + folderName + TARGET_FOLDER + "/differentfile-time.txt") //
							.setType(ActionType.COPY) //
			);
			// Run
			List<MirrorAction> returned = unitUnderTest.build(PREFIX + folderName + SOURCE_FOLDER,
					PREFIX + folderName + TARGET_FOLDER);
			// Check
			assertEquals(expected, returned);
		}

		@DisplayName("Returns an empty list of actions for a source folders with a file with an older modification "
				+ "date (time).")
		@Test
		void calledForASourceFolderWhichContainsAOlderDifferentFileTime_ReturnsAnEmptyListOfActions() throws Exception {
			// Prepare
			String folderName = "folderWithDifferentFileTime";
			new File(PREFIX + folderName + SOURCE_FOLDER + "/afile.txt").setLastModified(DATE);
			new File(PREFIX + folderName + TARGET_FOLDER + "/afile.txt").setLastModified(DATE);
			new File(PREFIX + folderName + SOURCE_FOLDER + "/differentfile-time.txt").setLastModified(DATE - 1000);
			new File(PREFIX + folderName + TARGET_FOLDER + "/differentfile-time.txt").setLastModified(DATE);
			List<MirrorAction> expected = new ArrayList<>();
			// Run
			List<MirrorAction> returned = unitUnderTest.build(PREFIX + folderName + SOURCE_FOLDER,
					PREFIX + folderName + TARGET_FOLDER);
			// Check
			assertEquals(expected, returned);
		}

		@DisplayName("Returns a list of actions for a target folder with one file more than the source folder.")
		@Test
		void calledForATargetFolderWithOneFileMoreThanTheSourceFolder_ReturnsAListOfActions() throws Exception {
			// Prepare
			String folderName = "folderWithFileToMuchInTarget";
			new File(PREFIX + folderName + SOURCE_FOLDER + "/afile.txt").setLastModified(DATE);
			new File(PREFIX + folderName + TARGET_FOLDER + "/afile.txt").setLastModified(DATE);
			new File(PREFIX + folderName + TARGET_FOLDER + "/file-to-much.txt").setLastModified(DATE);
			List<MirrorAction> expected = Arrays.asList( //
					new MirrorAction() //
							.setTargetFileName(PREFIX + folderName + TARGET_FOLDER + "/file-to-much.txt") //
							.setType(ActionType.REMOVE) //
			);
			// Run
			List<MirrorAction> returned = unitUnderTest.build(PREFIX + folderName + SOURCE_FOLDER,
					PREFIX + folderName + TARGET_FOLDER);
			// Check
			assertEquals(expected, returned);
		}

	}

}