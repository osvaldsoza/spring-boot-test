package com.github.osvaldsoza.springboottest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot Test API")
                        .version("1.0")
                        .description("Spring Boot Test API Description")
                        .termsOfService("http://spring.io")
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("http://spring.io")));
    }
}
