package com.ssi.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The main class of the LoginGovApplication Spring Boot application. This class
 * initializes and runs the Spring Boot application.
 *  @Author: munirathnam.surepall
 */
@SpringBootApplication
@ComponentScan({ "com.ssi.ms" })
public class LoginGovApplication {
	/**
	 * The main method to start the Spring Boot application.
	 *
	 * @param args The command line arguments passed to the application.
	 */
	public static void main(final String[] args) {
		SpringApplication.run(LoginGovApplication.class, args);
	}
}
