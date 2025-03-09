package com.proyectofinder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración para servir archivos estáticos, en especial los subidos en el directorio "uploads".
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Permitir servir archivos de la carpeta "uploads" en el sistema
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
