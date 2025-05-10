package com.ssi.ms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan({"com.ssi.ms",
        "com.ssi.service.core.platform"})
public class SsiTemplateDomainAndSubdomain {

    public static void main(final String[] args) {
        SpringApplication.run(SsiTemplateDomainAndSubdomain.class, args);
    }


}
