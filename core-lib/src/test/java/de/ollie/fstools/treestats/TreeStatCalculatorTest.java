package de.ollie.fstools.treestats;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests of class "TreeStatCalculator".
 *
 * @author ollie (02.01.2020)
 */
@ExtendWith(MockitoExtension.class)
public class TreeStatCalculatorTest {

	private PrintStream outStream = null;

	@InjectMocks
	private TreeStatCalculator unitUnderTest;

	@BeforeEach
	void setUp() {
		outStream = mock(PrintStream.class);
		TreeStatCalculator.out = outStream;
	}

	@AfterEach
	void tearDown() {
		TreeStatCalculator.out = System.out;
	}

	@DisplayName("Tests for method 'calculate(Path)'.")
	@Nested
	class TestForMethod_calculate_Path {

		@DisplayName("Throws an exception passing a null value as path.")
		@Test
		void passANullValueAsPath_ThrowsAnExeption() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.calculate(null));
		}

		@DisplayName("Returns the correct statistic information for the passed path.")
		@Test
		void passAValidPath_ReturnsTheCorrectTreeStatsObject() throws Exception {
			// Prepare
			TreeStats expected = new TreeStats() //
					.setFileCount(3) //
					.setFolderCount(3) //
					.setSize(0);
			// Run
			TreeStats returned = unitUnderTest.calculate(Paths.get("src", "test", "resources", "testfolder"))
					.setSize(0);
			// OLI: Folder sizes are different in different OS.
			// Check
			assertThat(returned, equalTo(expected));
		}

	}

	@DisplayName("Tests for method 'main(String[])'.")
	@Nested
	class TestForMethod_main_StringArray {

		@DisplayName("Throws an exception passing no path to calculate for.")
		@Test
		void passNotPathToCalculate_ThrowsAnExeption() {
			assertThrows(IllegalArgumentException.class, () -> TreeStatCalculator.main(new String[0]));
		}

		@DisplayName("Throws an exception passing a null value as arguments.")
		@Test
		void passANullValueAsArguments_ThrowsAnExeption() {
			assertThrows(IllegalArgumentException.class, () -> TreeStatCalculator.main(null));
		}

		@DisplayName("Passing a valid path prints the statistic information to the defined out stream.")
		@Test
		void passAValidArgument_PrintsTheStatisticInformationToTheOutStream() throws Exception {
			// Prepare
			String path = "src/test/resources/testfolder";
			// Run
			TreeStatCalculator.main(new String[] { path });
			// Check
			verify(outStream, times(2)).println();
			verify(outStream, times(1)).println("Statistic information for: " + path);
			verify(outStream, times(1)).println("Folders: " + 3);
			verify(outStream, times(1)).println("Files:   " + 3);
		}

	}

	@Nested
	class TestFileStats {

		@Test
		void test() throws Exception {
			Path path = Paths.get("pom.xml");
			BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
			System.out.printf("fileName         = %s%n", path.toAbsolutePath().toString());
			System.out.printf("creationTime     = %s%n", attributes.creationTime());
			System.out.printf("lastAccessTime   = %s%n", attributes.lastAccessTime());
			System.out.printf("lastModifiedTime = %s%n", attributes.lastModifiedTime());
			System.out.printf("size             = %s%n", attributes.size());
			System.out.printf("directory        = %s%n", attributes.isDirectory());
		}
	}
}
