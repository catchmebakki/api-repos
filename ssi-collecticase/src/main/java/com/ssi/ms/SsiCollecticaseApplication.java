package com.ssi.ms;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication
@ComponentScan({"com.ssi.ms"})
public class SsiCollecticaseApplication {

    @Value("${application.file-processor.thread.pool-size:1}")
    private int poolSize;
    public static void main(final String[] args) {
        SpringApplication.run(SsiCollecticaseApplication.class, args);
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(poolSize);
    }
}
