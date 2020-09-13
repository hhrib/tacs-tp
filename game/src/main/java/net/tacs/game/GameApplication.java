package net.tacs.game;

import net.tacs.game.model.Match;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GameApplication {

	private static List<Match> matches = new ArrayList<>();
	public static List<Province> provinces;
	private static List<Municipality> municipalities;
	private static List<User> users;

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

	public static List<Match> findMatches() {
		return matches;
	}

	public static List<Province> findProvinces() {
		return provinces;
	}

	public static List<Municipality> findMunicipalities() {
		return municipalities;
	}

	public static List<User> findUsers() {
		return users;
	}

	//Agregar los métodos para buscar/guardar en memoria que hagan falta (hasta que tengamos persistencia)

}
