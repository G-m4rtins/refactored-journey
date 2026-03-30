package com.example.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI backendOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Beneficios API")
                .version("v1")
                .description("API REST para gestao de beneficios e transferencias")
                .contact(new Contact()
                    .name("Candidato")
                    .email("candidato@example.com")));
    }
}
