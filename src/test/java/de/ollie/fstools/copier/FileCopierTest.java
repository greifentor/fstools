package de.ollie.fstools.copier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.fstools.Counter;

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
			String sourceContent = new String(Files.readAllBytes(Paths.get(sourcePath)));
			// Run
			unitUnderTest.copy(sourceFile, targetFile);
			// Check
			String targetContent = new String(Files.readAllBytes(Paths.get(targetPath)));
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
			String sourceContent = new String(Files.readAllBytes(Paths.get(sourcePath)));
			// Run
			unitUnderTest.copy(sourceFile, targetFile);
			// Check
			String targetContent = new String(Files.readAllBytes(Paths.get(targetPath)));
			assertEquals(sourceContent, targetContent);
		}

		@DisplayName("Fires an event if the file has been copied.")
		@Test
		void passCorrectFiles_FiresAnEvent(@TempDir Path tempDir) throws Exception {
			// Prepare
			unitUnderTest = new FileCopier();
			String sourcePath = "src/test/resources/testfolder/testFile.txt";
			String targetPath = tempDir + "/copiedFile.txt";
			File sourceFile = new File(sourcePath);
			File targetFile = new File(targetPath);
			unitUnderTest.addFileCopierListener(event -> {
				assertTrue(event.getAbsoluteSourcePathName().replace("\\", "/").endsWith(sourcePath));
				assertEquals(sourceFile.length(), event.getBytesCopied());
				assertEquals(0L, event.getBytesLeft());
			});
			// Run
			unitUnderTest.copy(sourceFile, targetFile);
		}

		@DisplayName("Fires an event if the file has been copied (with alternate buffer size).")
		@Test
		void passCorrectFiles_WithAlternateBufferSize_FiresAnEvent(@TempDir Path tempDir) throws Exception {
			// Prepare
			unitUnderTest = new FileCopier(1);
			Counter counter = new Counter();
			unitUnderTest.addFileCopierListener(event -> counter.inc());
			String sourcePath = "src/test/resources/testfolder/testFile.txt";
			String targetPath = tempDir + "/copiedFile.txt";
			File sourceFile = new File(sourcePath);
			File targetFile = new File(targetPath);
			// Run
			unitUnderTest.copy(sourceFile, targetFile);
			// Check
			assertThat((long) counter.getCount(), equalTo(sourceFile.length()));
		}

	}

}
