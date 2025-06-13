package com.example.PharmaPhorm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // libera todas as rotas
                        .allowedOrigins("http://localhost:8081") // substitua pela origem do seu front-end
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // métodos permitidos
                        .allowedHeaders("*");
            }
        };
    }
}
