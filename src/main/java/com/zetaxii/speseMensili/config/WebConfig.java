package com.zetaxii.speseMensili.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aggiungi il mapping per tutte le rotte
                .allowedOrigins("*") // L'URL della tua app frontend Ionic
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Metodi permessi
                .allowedHeaders("*"); // Header permessi
    }
}
