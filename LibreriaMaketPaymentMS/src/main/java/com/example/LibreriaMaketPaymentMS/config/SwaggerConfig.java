package com.example.LibreriaMaketPaymentMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(new Info()
                                        .title("LM Pago API")
                                        .version("v1")
                                        .description("Documentacion para API de gestion de pagos Libreria Market"));
    }
}

