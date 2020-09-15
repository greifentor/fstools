package de.ollie.fstools.shell.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.filestats.FileStatsReader;
import de.ollie.fstools.mirror.ActionListBuilder;
import de.ollie.fstools.mirror.CopyFilter;
import de.ollie.fstools.mirror.ExcludeActionFilter;
import de.ollie.fstools.mirror.filters.CopyAtAnyTimeFileNamePatternCopyFilter;
import de.ollie.fstools.shell.service.FSToolsService;
import de.ollie.fstools.shell.service.converter.MirrorActionSOFromMirrorActionConverter;
import de.ollie.fstools.shell.service.so.MirrorActionSO;

/**
 * An implementation of the FSToolsService interface.
 *
 * @author ollie (11.09.2020)
 */
@Service
public class FSToolsServiceImpl implements FSToolsService {

	private final ActionListBuilder actionListBuilder;
	private final FileStatsReader fileStatsReader;
	private final MirrorActionSOFromMirrorActionConverter mirrorActionConverter;

	public FSToolsServiceImpl(ActionListBuilder actionListBuilder, FileStatsReader fileStatsReader,
			MirrorActionSOFromMirrorActionConverter mirrorActionConverter) {
		super();
		this.actionListBuilder = actionListBuilder;
		this.fileStatsReader = fileStatsReader;
		this.mirrorActionConverter = mirrorActionConverter;
	}

	@Override
	public List<MirrorActionSO> buildActionList(String sourcePathName, String targetPathName,
			List<String> excludePatterns, List<String> copyAtAnyTimePatterns) throws IOException {
		return actionListBuilder
				.build(sourcePathName, targetPathName, getCopyFilters(copyAtAnyTimePatterns),
						getExcludeActionFilters(excludePatterns)) //
				.stream() //
				.map(mirrorActionConverter::convert) //
				.collect(Collectors.toList()) //
		;
	}

	private List<CopyFilter> getCopyFilters(List<String> copyAtAnyTimePatterns) {
		return copyAtAnyTimePatterns //
				.stream() //
				.map(s -> new CopyAtAnyTimeFileNamePatternCopyFilter(s)) //
				.collect(Collectors.toList()) //
		;
	}

	private ExcludeActionFilter[] getExcludeActionFilters(List<String> excludePatterns) {
		return new ExcludeActionFilter[0];
	}

	@Override
	public FileStats getFileStats(String path) throws IOException {
		return fileStatsReader.read(path);
	}

}