package de.ollie.fstools.copier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for class "FileCopier".
 *
 * @author ollie (03.01.2020)
 */
@ExtendWith(MockitoExtension.class)
public class FileCopierTest {

	@InjectMocks
	private FileCopier unitUnderTest;

	@DisplayName("Tests of the method 'copy(File,File)'.")
	@Nested
	class TestsOfMethod_copy_File_File {

		@DisplayName("Throws an exception passing source file as null value.")
		@Test
		void passSourceFileAsNullValue_ThrowsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.copy(null, new File("/")));
		}

		@DisplayName("Throws an exception passing target file as null value.")
		@Test
		void passTargetFileAsNullValue_ThrowsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.copy(new File("/"), null));
		}

		@DisplayName("Throws an exception passing a not existing source file.")
		@Test
		void passANotExistingSourceFile_ThrowsAnException() {
			assertThrows(FileNotFoundException.class,
					() -> unitUnderTest.copy(new File("@:NotExisting"), new File("/target.file")));
		}

		@DisplayName("Copies the source file into the target file.")
		@Test
		void passCorrectFiles_CopiesTheSourceFileContentIntoTheTargetFile(@TempDir Path tempDir) throws Exception {
			// Prepare
			String sourcePath = "src/test/resources/testfolder/testFile.txt";
			String targetPath = tempDir + "/copiedFile.txt";
			File sourceFile = new File(sourcePath);
			File targetFile = new File(targetPath);
			String sourceContent = Files.readString(Path.of(sourcePath));
			// Run
			unitUnderTest.copy(sourceFile, targetFile);
			// Check
			String targetContent = Files.readString(Path.of(targetPath));
			assertEquals(sourceContent, targetContent);
		}

		@DisplayName("Copies the source file into the target file (with alternate buffer size).")
		@Test
		void passCorrectFiles_WithAlternateBufferSize_CopiesTheSourceFileContentIntoTheTargetFile(@TempDir Path tempDir)
				throws Exception {
			// Prepare
			unitUnderTest = new FileCopier(3);
			String sourcePath = "src/test/resources/testfolder/testFile.txt";
			String targetPath = tempDir + "/copiedFile.txt";
			File sourceFile = new File(sourcePath);
			File targetFile = new File(targetPath);
			String sourceContent = Files.readString(Path.of(sourcePath));
			// Run
			unitUnderTest.copy(sourceFile, targetFile);
			// Check
			String targetContent = Files.readString(Path.of(targetPath));
			assertEquals(sourceContent, targetContent);
		}

	}

}