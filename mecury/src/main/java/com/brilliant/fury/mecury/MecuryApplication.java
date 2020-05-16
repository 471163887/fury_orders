package com.brilliant.fury.mecury;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.brilliant.fury.mecury.mapper")
public class MecuryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MecuryApplication.class, args);
	}

}
