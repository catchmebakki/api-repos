package com.ssi.login.gov.apis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.ssi.login.gov.apis" })
public class LoginGovApplication {

	public static void main(final String[] args) {
		SpringApplication.run(LoginGovApplication.class, args);
	}
}



