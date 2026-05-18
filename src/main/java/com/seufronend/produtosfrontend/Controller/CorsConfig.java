package com.seufronend.produtosfrontend.Controller; // AJUSTE O PACKAGE CONFORME SEU PROJETO

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/produtos/**")
                .allowedOrigins(
                        "https://produtos-frontend-o38v.onrender.com",
                        "http://localhost:8080",
                        "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}