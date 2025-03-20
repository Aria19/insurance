package com.maghrebia.data_extract.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Donation Project API")
                .version("1.0")
                .description("API documentation for the Donation Web App"))
            .addSecurityItem(new SecurityRequirement().addList("BearerAuth")) // 🔹 Apply security globally
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("BearerAuth", new SecurityScheme()
                    .name("BearerAuth")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT") // 🔹 This tells Swagger to use JWT
                ));
    }
    
}
