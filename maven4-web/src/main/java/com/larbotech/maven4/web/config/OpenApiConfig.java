package com.larbotech.maven4.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration OpenAPI/Swagger
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Maven 4 Demo API")
                .version("1.0.0")
                .description("API de démonstration des fonctionnalités Maven 4 avec Spring Boot 3")
                .contact(new Contact()
                    .name("Larbotech")
                    .email("contact@larbotech.com")));
    }
}
