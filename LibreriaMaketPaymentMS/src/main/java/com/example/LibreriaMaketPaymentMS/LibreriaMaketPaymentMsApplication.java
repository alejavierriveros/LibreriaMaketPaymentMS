package com.example.LibreriaMaketPaymentMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableDiscoveryClient //Viene incluida por defecto la anotación en Spring Boot moderno, pero se recomienda explicitarla para tener claro lo que estamos utilizando.
@EnableFeignClients
@SpringBootApplication
public class LibreriaMaketPaymentMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibreriaMaketPaymentMsApplication.class, args);
	}

}
