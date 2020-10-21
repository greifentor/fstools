package de.ollie.fstools.mirror;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FreeMemProviderTest {

	@InjectMocks
	private FreeMemProvider unitUnderTest;

	@DisplayName("Returns a value greater zero.")
	@Test
	void returnsAValueGreaterZero() {
		assertTrue(unitUnderTest.freeMemory() > 0);
	}

}