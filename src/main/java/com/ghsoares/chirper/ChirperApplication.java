package com.ghsoares.chirper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChirperApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChirperApplication.class, args);
	}

//	@Bean
//	public ObjectMapper objectMapper() {
//	    ObjectMapper objectMapper = new ObjectMapper();
//	    objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
//	    return objectMapper;
//	}
}
