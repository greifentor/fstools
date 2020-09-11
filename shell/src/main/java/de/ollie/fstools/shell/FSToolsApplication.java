package de.ollie.fstools.shell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Starter class for the fstools shell.
 * 
 * @author ollie (11.09.2020)
 */
@SpringBootApplication
@ComponentScan("de.ollie")
public class FSToolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FSToolsApplication.class, args);
	}

}