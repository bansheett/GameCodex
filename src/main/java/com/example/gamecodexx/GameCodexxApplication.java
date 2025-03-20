package com.example.gamecodexx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GameCodexxApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameCodexxApplication.class, args);
	}

}