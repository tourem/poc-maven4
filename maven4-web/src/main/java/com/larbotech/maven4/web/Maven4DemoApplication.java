package com.larbotech.maven4.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application principale pour démontrer Maven 4
 */
@SpringBootApplication(scanBasePackages = "com.larbotech.maven4")
@EntityScan("com.larbotech.maven4.common.entity")
@EnableJpaRepositories("com.larbotech.maven4.service.repository")
public class Maven4DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Maven4DemoApplication.class, args);
    }
}
