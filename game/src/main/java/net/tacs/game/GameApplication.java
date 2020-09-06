package net.tacs.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GameApplication {

	@RequestMapping("/")
	public String home() {
		return "¡Bienvenidos a TACticaS, el juego de la pandemia!";
	}

	public static void main(String[] args) {
		SpringApplication.run(GameApplication.class, args);
	}

}
