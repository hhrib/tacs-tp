package net.tacs.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tacs.game.model.Match;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import net.tacs.game.services.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

	private static List<Match> matches = new ArrayList<>();
	private static List<Province> provinces = new ArrayList<>();
	private static List<Municipality> municipalities = new ArrayList<>();
	private static List<User> users = new ArrayList<>();
	private static String auth0Token;

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private ProvinceService provinceService;

	public static void main(String[] args) {
		SpringApplication.run(GameApplication.class, args);
	}

	public static void addMatch(Match newMatch) {
		matches.add(newMatch);
	}

	public static void addProvince(Province province) {
		provinces.add(province);
	}

	public static void addMunicipality(Municipality newMunicipality) {
		municipalities.add(newMunicipality);
	}

	public static void addUser(User newUser) {
		users.add(newUser);
	}

	public static void setUsers(List<User> newUsers) {
		users = newUsers;
	}

	public static void setToken(String token) {
		auth0Token = token;
	}

	public static List<Match> getMatches()
	{
		return matches;
	}

	public static List<Province> getProvinces() {
		return provinces;
	}

	public static List<Municipality> getMunicipalities() {
		return municipalities;
	}

	public static List<User> getUsers() {
		return users;
	}

	public static String getToken() {
		return auth0Token;
	}


	//Agregar los métodos para buscar/guardar en memoria que hagan falta (hasta que tengamos persistencia)
}
