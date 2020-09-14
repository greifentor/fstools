package de.ollie.fstools.shell.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.filestats.FileStatsReader;
import de.ollie.fstools.mirror.ActionListBuilder;
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

	@Autowired
	private MirrorActionSOFromMirrorActionConverter mirrorActionConverter;

	private static final ActionListBuilder actionListBuilder = new ActionListBuilder();
	private static final FileStatsReader fileStatsReader = new FileStatsReader();

	@Override
	public FileStats getFileStats(String path) throws IOException {
		return fileStatsReader.read(path);
	}

	@Override
	public List<MirrorActionSO> buildActionList(String sourcePathName, String targetPathName) throws IOException {
		return actionListBuilder.build(sourcePathName, targetPathName, new ArrayList<>()) //
				.stream() //
				.map(mirrorActionConverter::convert) //
				.collect(Collectors.toList()) //
		;
	}

}