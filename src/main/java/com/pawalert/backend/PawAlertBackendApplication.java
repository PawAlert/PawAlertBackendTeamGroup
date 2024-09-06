package com.pawalert.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableMongoRepositories(basePackages = {
        "com.pawalert.backend.domain.comment.repository",
        "com.pawalert.backend.domain.notification.repository"
})
public class PawAlertBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PawAlertBackendApplication.class, args);
    }
}
