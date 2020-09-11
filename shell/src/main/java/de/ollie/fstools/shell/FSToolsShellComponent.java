package de.ollie.fstools.shell;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import de.ollie.fstools.filestats.FileStats;
import de.ollie.fstools.shell.service.FSToolsService;

/**
 * The shell component for the fstools.
 *
 * @author ollie (11.09.2020)
 */
@ShellComponent
public class FSToolsShellComponent {

	private static final String NL = "\n";

	@Autowired
	private FSToolsService fsToolsService;

	@ShellMethod("Shows the file stats for the passed path")
	public String fs(String path) {
		try {
			return fileStatsToString(fsToolsService.getFileStats(path));
		} catch (Exception e) {
			return "error: " + e.getMessage();
		}
	}

	private String fileStatsToString(FileStats fileStats) {
		return String.format("%s%n" //
				+ "%s%n" //
				+ "%d bytes%n" //
				+ "modified at %s%n", //
				fileStats.getName(), //
				String.valueOf(fileStats.getType()), //
				fileStats.getSize(), //
				String.valueOf(fileStats.getLastModifiedTime()));
	}

	@ShellMethod("Shows the file stats of all file in the path")
	public String ls(String path) {
		try {
			String[] pathes = new File(path).list();
			return fileStatsToStringTable(path, pathes);
		} catch (Exception e) {
			return "error: " + e.getMessage();
		}
	}

	private String fileStatsToStringTable(String basePath, String[] pathes) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (String path : pathes) {
			FileStats fileStats = fsToolsService
					.getFileStats(basePath.replace("\\", "/") + "/" + path.replace("\\", "/"));
			sb.append(String.format("%-10s %10d %s%n", String.valueOf(fileStats.getType()), fileStats.getSize(),
					fileStats.getName()));
		}
		return sb.toString();
	}

}