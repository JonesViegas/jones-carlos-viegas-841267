package br.gov.mt.seplag.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArtistManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtistManagerApplication.class, args);
    }
}