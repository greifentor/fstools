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
import de.ollie.fstools.mirror.MirrorAction;
import de.ollie.fstools.mirror.MirrorActionProcessor;
import de.ollie.fstools.mirror.filters.CopyAtAnyTimeFileNamePatternCopyFilter;
import de.ollie.fstools.mirror.filters.ExclusionContainedInFileNameExcludeActionFilter;
import de.ollie.fstools.shell.service.FSToolsService;
import de.ollie.fstools.shell.service.converter.MirrorActionFromMirrorActionSOConverter;
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
	private final MirrorActionProcessor mirrorActionProcessor;
	private final MirrorActionSOFromMirrorActionConverter mirrorActionSOFromModelConverter;
	private final MirrorActionFromMirrorActionSOConverter mirrorActionFromSOConverter;

	public FSToolsServiceImpl(ActionListBuilder actionListBuilder, FileStatsReader fileStatsReader,
			MirrorActionProcessor mirrorActionProcessor,
			MirrorActionSOFromMirrorActionConverter mirrorActionSOFromModelConverter,
			MirrorActionFromMirrorActionSOConverter mirrorActionFromSOConverter) {
		super();
		this.actionListBuilder = actionListBuilder;
		this.fileStatsReader = fileStatsReader;
		this.mirrorActionProcessor = mirrorActionProcessor;
		this.mirrorActionFromSOConverter = mirrorActionFromSOConverter;
		this.mirrorActionSOFromModelConverter = mirrorActionSOFromModelConverter;
	}

	@Override
	public List<MirrorActionSO> buildActionList(String sourcePathName, String targetPathName,
			List<String> excludePatterns, List<String> copyAtAnyTimePatterns) throws IOException {
		return actionListBuilder
				.build(sourcePathName, targetPathName, getCopyFilters(copyAtAnyTimePatterns),
						getExcludeActionFilters(excludePatterns)) //
				.stream() //
				.map(mirrorActionSOFromModelConverter::convert) //
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
		return excludePatterns //
				.stream() //
				.map(s -> new ExclusionContainedInFileNameExcludeActionFilter(s)) //
				.collect(Collectors.toList()) //
				.toArray(new ExclusionContainedInFileNameExcludeActionFilter[excludePatterns.size()]) //
		;
	}

	@Override
	public FileStats getFileStats(String path) throws IOException {
		return fileStatsReader.read(path);
	}

	@Override
	public void processMirrorActions(List<MirrorActionSO> actionsSO) throws IOException {
		List<MirrorAction> actions = actionsSO //
				.stream() //
				.map(mirrorActionFromSOConverter::convert) //
				.collect(Collectors.toList()) //
		;
		mirrorActionProcessor.processMirrorActions(actions);
	}

}