package de.ollie.fstools.shell.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import de.ollie.fstools.shell.service.TouchService;
import de.ollie.fstools.touch.Touch;

/**
 * An implementation of the touch service interface.
 *
 * @author ollie (20.10.2020)
 */
@Service
public class TouchServiceImpl implements TouchService {

	private Touch touch = new Touch();

	@Override
	public void touch(String fileName, LocalDateTime lastModifiedTime) throws IOException {
		touch.touch(fileName, lastModifiedTime);
	}

}