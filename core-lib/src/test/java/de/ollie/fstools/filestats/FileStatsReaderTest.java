package de.ollie.fstools.filestats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.fstools.filestats.FileStats.FileType;

@ExtendWith(MockitoExtension.class)
public class FileStatsReaderTest {

	@InjectMocks
	private FileStatsReader unitUnderTest;

	@DisplayName("Tests for method 'read(String)'.")
	@Nested
	class TestsForMethod_read_String {

		@DisplayName("Throws an exception if a null value is passed.")
		@Test
		void nullPassed_ThrowsAnException() {
			assertThrows(NullPointerException.class, () -> unitUnderTest.read(null));
		}

		@DisplayName("Reads the correct stats for the file with the passed name.")
		@Test
		void fileNamePassed_ReturnsAStatsObjectForTheFileWithThePassedName() throws Exception {
			// Prepare
			String fileName = "src/test/resources/testfolder/testFile.txt";
			Instant instant = Instant.ofEpochMilli(
					Files.getLastModifiedTime(Paths.get(fileName), LinkOption.NOFOLLOW_LINKS).toMillis() / 1000 * 1000);
			LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
			FileStats expected = new FileStats() //
					.setLastModifiedTime(date) //
					.setName(fileName) //
					.setSize(10) //
					.setType(FileType.FILE) //
			;
			// Run
			FileStats returned = unitUnderTest.read(fileName);
			// Check
			assertEquals(expected, returned);
		}

		@DisplayName("Reads the correct stats for the directory with the passed name.")
		@Test
		void directoryNamePassed_ReturnsAStatsObjectForTheDirectoryWithThePassedName() throws Exception {
			// Prepare
			String fileName = "src/test/resources/testfolder";
			Instant instant = Instant.ofEpochMilli(
					Files.getLastModifiedTime(Paths.get(fileName), LinkOption.NOFOLLOW_LINKS).toMillis() / 1000 * 1000);
			LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
			FileStats expected = new FileStats() //
					.setLastModifiedTime(date) //
					.setName(fileName) //
					.setSize(0) //
					.setType(FileType.DIRECTORY) //
			;
			// Run
			FileStats returned = unitUnderTest.read(fileName).setSize(0);
			// OLI: Folder sizes are different in different OS.
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for method 'main(String[])'.")
	@Nested
	class TestsForMethod_main_String {

		@DisplayName("Works for an existing file.")
		@Test
		void fileNamePassed_DoesNotLeadToAnError() throws Exception {
			// Prepare
			String fileName = "src/test/resources/testfolder/testFile.txt";
			// Run & Check
			try {
				FileStatsReader.main(new String[] { fileName });
			} catch (Exception e) {
				fail("should not throw an exception.");
			}
		}

		@DisplayName("Works for a not existing file.")
		@Test
		void fileNameOfANotExistingFilePassed_DoesNotLeadToAnError() throws Exception {
			// Prepare
			String fileName = "src/test/resources/testfolder/NOT.EXISTING";
			// Run & Check
			try {
				FileStatsReader.main(new String[] { fileName });
			} catch (Exception e) {
				fail("should not throw an exception.");
			}
		}

	}

}