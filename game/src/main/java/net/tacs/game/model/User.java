package net.tacs.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    public User() {
//        String generatedString = generateRandomString();
//        this.id = generatedString;
    }

    //TODO Random para el id es temporal hasta que implementemos persistencia
    public User(String username) {
//        this.id = generateRandomString();
        this.username = username;
    }

    //TODO ver si es auto generado, por ahora lo necesito en los tests
    public void setId(String id) {
        this.id = id;
    }

    //@Id @GeneratedValue
    private String id;
    private String username;
    private boolean playing;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    private String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    public int municipalitiesOwning(List<Municipality> munisInMatch)
    {
        int munisOwned = 0;

        for (Municipality aMuni : munisInMatch) {
            if(aMuni.getOwner().equals(this))
                munisOwned++;
        }

        return munisOwned;
    }

    public List<Municipality> getMunicipalitiesOwning(List<Municipality> munisInMatch)
    {
        List<Municipality> owningMunis = new ArrayList<>();

        for (Municipality aMuni : munisInMatch) {
            if(aMuni.getOwner().equals(this))
                owningMunis.add(aMuni);
        }

        return owningMunis;
    }

    public boolean isAvailable(List<Match> matches)
    {
        //si el jugador esta en una partida en progreso
        for (Match aMatch : matches) {
            if(aMatch.getUsers().contains(this))
                return false;
        }

        //si el jugador esta disponible para una nueva partida
        return true;
    }
}
