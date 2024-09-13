package com.tta.geumgiri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class GeumgiriApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeumgiriApplication.class, args);
	}

}
