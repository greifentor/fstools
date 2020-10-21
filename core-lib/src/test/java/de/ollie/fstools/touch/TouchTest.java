package de.ollie.fstools.touch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TouchTest {

	private static final LocalDateTime NOW = LocalDateTime.of(2020, 10, 19, 23, 50, 40);

	@InjectMocks
	private Touch unitUnderTest;

	@DisplayName("Test of method 'touch(String, LocalDateTime, LocalDateTime)'.")
	@Nested
	class TestOfMethod_touch_String_LocalDateTime_LocalDateTime {

		@DisplayName("Throws an exception if a null value is passed as file name.")
		@Test
		void passNullValueAsFileName_ThrowsAnException() {
			assertThrows(NullPointerException.class, () -> unitUnderTest.touch(null, NOW));
		}

		@DisplayName("Throws an exception if the passed string does not contain the name for an existing file.")
		@Test
		void passNameOfANotExistingFile_ThrowsAnException() {
			assertThrows(NoSuchFileException.class, () -> unitUnderTest.touch("/not/existing/file.name", NOW));
		}

		@DisplayName("Throws an exception if the passed last access time is passed as null.")
		@Test
		void passNullValueAsLastAccessTime_ThrowsAnException() {
			assertThrows(NullPointerException.class, () -> unitUnderTest.touch("/not/existing/file.name", null));
		}

		@DisplayName("Sets the last modification and access time for the passed file.")
		@Test
		void passValidFileNameAndTimestamps_SetsTheLastModificationAndAccessTimeForTheFileWithThePassedName(
				@TempDir Path dir) throws Exception {
			// Prepare
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				fail("thread sleep interrupted.");
			}
			String pathName = dir + "/test.file";
			Path path = Paths.get(pathName);
			Files.write(path, "test file content".getBytes(), StandardOpenOption.CREATE);
			// Run
			unitUnderTest.touch(pathName, NOW);
			// Check
			BasicFileAttributes attrsTouched = Files.readAttributes(path, BasicFileAttributes.class);
			FileTime returned = attrsTouched.lastModifiedTime();
			FileTime expected = FileTime.fromMillis(NOW.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			assertEquals(expected, returned);
		}

	}

}