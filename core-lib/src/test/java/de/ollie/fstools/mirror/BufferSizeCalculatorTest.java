package de.ollie.fstools.mirror;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BufferSizeCalculatorTest {

	private static final long SOURCE_FILE_LENGTH = 4711;

	@Mock
	private FreeMemProvider freeMemProvider;

	private BufferSizeCalculator unitUnderTest;

	@BeforeEach
	private void setUp() {
		unitUnderTest = new BufferSizeCalculator(freeMemProvider);
	}

	@DisplayName("Tests for method 'calc(long)'.")
	@Nested
	class TestsForMethod_calc_long {

		@DisplayName("Returns 80% of Integer.MAX_VALUE if the free memory exceeds the integer maximum value and source "
				+ "file length also.")
		@Test
		void freeMemoryAndSourceFileLengthIsGreaterThanMaximumInteger_Returns80PercentOfIntegerMAX_VALUE() {
			// Prepare
			int expected = (int) (Integer.MAX_VALUE * 0.8);
			when(freeMemProvider.freeMemory()).thenReturn(((long) Integer.MAX_VALUE) + 1);
			// Run
			int returned = unitUnderTest.calc(((long) Integer.MAX_VALUE) + 1);
			// Check
			assertEquals(expected, returned);
		}

		@DisplayName("Returns 80% of free memory if the free memory is smaller than the integer maximum value but "
				+ "source file length is not.")
		@Test
		void freeMemoryIsSmallerThanMaximumIntegerButFileSizeIsNot_Returns80PercentOfTheFreeMem() {
			// Prepare
			int expected = 800;
			long freeMem = 1000;
			when(freeMemProvider.freeMemory()).thenReturn(freeMem);
			// Run
			int returned = unitUnderTest.calc(freeMem * 2);
			// Check
			assertEquals(expected, returned);
		}

		@DisplayName("Returns the source file size if it ist smaller than free memory.")
		@Test
		void sourceFileSizeIsSmallerThanFreeMemory_ReturnsSourceFileSize() {
			// Prepare
			int expected = 1000;
			long freeMem = expected;
			when(freeMemProvider.freeMemory()).thenReturn(freeMem * 2);
			// Run
			int returned = unitUnderTest.calc(freeMem);
			// Check
			assertEquals(expected, returned);
		}

	}

}