package net.tacs.game.repositories;

import net.tacs.game.GameApplication;
import net.tacs.game.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Repository("userRepository")
//public interface UserRepository {//extends JpaRepository<User, Long> {

//@Repository("matchRepository")
//TODO Cambiar a interface repository cuando implementemos persistencia
@Component("userRepository")
public class UserRepository {

    private static List<User> users = new ArrayList<>();

    public Optional<User> findById(String id)
    {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public Optional<User> findByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void addUser(User newUser) {
        users.add(newUser);
    }

    public static void setUsers(List<User> newUsers) {
        users = newUsers;
    }
}
