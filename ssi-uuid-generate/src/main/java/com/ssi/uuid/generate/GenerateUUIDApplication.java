package com.ssi.uuid.generate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.ssi.uuid.generate" })
public class GenerateUUIDApplication {

	public static void main(final String[] args) {
		SpringApplication.run(GenerateUUIDApplication.class, args);
	}
}



