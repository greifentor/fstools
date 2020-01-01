package de.ollie.fstools;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for class "FileSystemTreeTraversal".
 *
 * @author ollie (01.01.2020)
 */
@ExtendWith(MockitoExtension.class)
public class FileSystemTreeTraversalTest {

	private static final Path PATH = Path.of("/");

	private FileSystemTreeTraversal unitUnderTest;

	@DisplayName("Tests of method 'constructor(Path)'.")
	@Nested
	class TestsOfMethod_constructor_Path {

		@DisplayName("Throws an exception passing a null value as path.")
		@Test
		void passANullValueAsPath_ThrowsAnException() {
			assertThrows(IllegalArgumentException.class, () -> new FileSystemTreeTraversal(null));
		}

		@DisplayName("Sets the passed path as working path for the traversal.")
		@Test
		void passAPathObject_SetsThePassedPathAsWorkingPathForTheTraversal() {
			// run
			unitUnderTest = new FileSystemTreeTraversal(PATH);
			// check
			assertThat(unitUnderTest.getWorkingPath(), equalTo(PATH));
		}

	}

	@DisplayName("Tests of method 'traverse()'.")
	@Nested
	class TestsOfMethod_traverse {

		@DisplayName("Throws an exception if the working path is not existing.")
		@Test
		@Disabled
		void workingPathIsNotExisting_ThrowsAnException() {
			assertThrows(FileNotFoundException.class,
					() -> new FileSystemTreeTraversal(Path.of("@:NotExisting")).traverse());
		}

	}

}