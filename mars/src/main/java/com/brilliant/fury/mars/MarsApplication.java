package com.brilliant.fury.mars;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.brilliant.fury.mars.mapper")
public class MarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarsApplication.class, args);
	}

}
