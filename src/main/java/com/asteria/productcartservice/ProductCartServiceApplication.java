package com.asteria.productcartservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProductCartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductCartServiceApplication.class, args);
	}

}
