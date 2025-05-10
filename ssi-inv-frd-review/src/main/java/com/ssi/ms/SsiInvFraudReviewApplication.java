package com.ssi.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.ssi.ms"})
public class SsiInvFraudReviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsiInvFraudReviewApplication.class, args);
	}
}