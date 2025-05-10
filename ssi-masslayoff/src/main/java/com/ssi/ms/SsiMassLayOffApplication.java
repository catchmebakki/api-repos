package com.ssi.ms;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Praveenraja Paramsivam
 * Main Spring Boot application class for the SSI Mass Layoff application.
 * Configures the application as a Spring Boot application and specifies the base package to scan for components.
 */
@SpringBootApplication
@ComponentScan({"com.ssi.ms"})
public class SsiMassLayOffApplication {

	/**
	 * The pool size for the file processor thread pool. The value is retrieved from the application properties.
	 * If not specified, a default value of 1 is used.
	 */
    @Value("${application.file-processor.thread.pool-size:1}")
    private int poolSize;

    /**
     * The main entry point of the SSI Mass LayOff Application.
     * @param args The command-line arguments passed to the application.
     */
    public static void main(final String[] args) {
        SpringApplication.run(SsiMassLayOffApplication.class, args);
    }

    /**
     * Creates and returns an ExecutorService instance.
     * @return {@link ExecutorService} ExecutorService instance created.
     */
    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(poolSize);
    }

}
