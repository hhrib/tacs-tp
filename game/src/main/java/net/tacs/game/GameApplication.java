package net.tacs.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class GameApplication {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	private static String auth0Token;

	public static void main(String[] args) {
		SpringApplication.run(GameApplication.class, args);
	}

	public static void setToken(String token) {
		auth0Token = token;
	}

	public static String getToken() {
		return auth0Token;
	}
}
