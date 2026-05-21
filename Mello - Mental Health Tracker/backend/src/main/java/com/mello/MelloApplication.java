package com.mello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(MelloApplication.class, args);
	}

}
