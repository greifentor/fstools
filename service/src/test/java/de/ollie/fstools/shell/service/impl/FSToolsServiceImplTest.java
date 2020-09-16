package de.ollie.fstools.shell.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.filestats.FileStatsReader;
import de.ollie.fstools.mirror.ActionListBuilder;
import de.ollie.fstools.mirror.CopyFilter;
import de.ollie.fstools.mirror.ExcludeActionFilter;
import de.ollie.fstools.mirror.filters.CopyAtAnyTimeFileNamePatternCopyFilter;
import de.ollie.fstools.shell.service.converter.MirrorActionSOFromMirrorActionConverter;
import de.ollie.fstools.shell.service.so.MirrorActionSO;

@ExtendWith(MockitoExtension.class)
public class FSToolsServiceImplTest {

	private static final String SOURCE_PATH_NAME = "sourcePathName";
	private static final String TARGET_PATH_NAME = "targetPathName";

	@Mock
	private ActionListBuilder actionListBuilder;
	@Mock
	private FileStatsReader fileStatsReader;
	@Mock
	private MirrorActionSOFromMirrorActionConverter mirrorActionConverter;

	@InjectMocks
	private FSToolsServiceImpl unitUnderTest;

	@DisplayName("Tests of method 'buildActionList(String)'.")
	@Nested
	class TestsOfMethod_buildActionList_String_String_ListString_ListString {

		@DisplayName("Calls the 'build' method of the action list builder with the correct parameters (no filters "
				+ "passed).")
		@Test
		void callsTheBuildMethodOfTheActionListBuilderWithCorrectParametersNoFiltersPassed() throws Exception {
			// Prepare
			List<MirrorActionSO> expected = new ArrayList<>();
			when(actionListBuilder.build(SOURCE_PATH_NAME, TARGET_PATH_NAME, null, new ArrayList<>(),
					new ExcludeActionFilter[0])).thenReturn(new ArrayList<>());
			// Run
			List<MirrorActionSO> returned = unitUnderTest.buildActionList(SOURCE_PATH_NAME, TARGET_PATH_NAME, null,
					new ArrayList<>(), new ArrayList<>());
			// Check
			assertEquals(expected, returned);
		}

		@DisplayName("Calls the 'build' method of the action list builder with the correct parameters (copy at any "
				+ "time file name patterns passed).")
		@Test
		void callsTheBuildMethodOfTheActionListBuilderWithCorrectParametersCopyAtAnyTimeFileNamePatternsPassed()
				throws Exception {
			// Prepare
			List<MirrorActionSO> expected = new ArrayList<>();
			String pattern1 = "pattern1";
			String pattern2 = "pattern2";
			List<String> copyAtAnyTimeFileNamePatterns = Arrays.asList(pattern1, pattern2);
			List<CopyFilter> copyFilters = Arrays.asList(new CopyAtAnyTimeFileNamePatternCopyFilter(pattern1),
					new CopyAtAnyTimeFileNamePatternCopyFilter(pattern2));
			when(actionListBuilder.build(SOURCE_PATH_NAME, TARGET_PATH_NAME, null, copyFilters,
					new ExcludeActionFilter[0])).thenReturn(new ArrayList<>());
			// Run
			List<MirrorActionSO> returned = unitUnderTest.buildActionList(SOURCE_PATH_NAME, TARGET_PATH_NAME, null,
					new ArrayList<>(), copyAtAnyTimeFileNamePatterns);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests of method 'getFileStats(String)'.")
	@Nested
	class TestsOfMethod_getFileStats_String {

		@DisplayName("Calls the right method of the file stats reader.")
		@Test
		void callsTheReadMethodOfTheFileStatsReader() throws Exception {
			// Prepare
			FileStats expected = new FileStats();
			String pathName = "pathName";
			when(fileStatsReader.read(pathName)).thenReturn(expected);
			// Run
			FileStats returned = unitUnderTest.getFileStats(pathName);
			// Check
			assertSame(expected, returned);
		}

	}

}