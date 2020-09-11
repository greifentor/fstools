package de.ollie.fstools.shell.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.filestats.FileStatsReader;
import de.ollie.fstools.shell.service.FSToolsService;

/**
 * An implementation of the FSToolsService interface.
 *
 * @author ollie (11.09.2020)
 */
@Service
public class FSToolsServiceImpl implements FSToolsService {

	private static final FileStatsReader fileStatsReader = new FileStatsReader();

	@Override
	public FileStats getFileStats(String path) throws IOException {
		return fileStatsReader.read(path);
	}

}