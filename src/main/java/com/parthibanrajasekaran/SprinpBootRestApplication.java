package com.parthibanrajasekaran;

import com.parthibanrajasekaran.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SprinpBootRestApplication {
//public class SprinpBootRestApplication implements CommandLineRunner {

    @Autowired
    CatalogRepository catalogRepository;

    public static void main(String[] args) {
        SpringApplication.run(SprinpBootRestApplication.class, args);
    }

}
