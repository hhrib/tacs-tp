package net.tacs.game.repositories;

import net.tacs.game.GameApplication;
import net.tacs.game.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository("userRepository")
//public interface UserRepository {//extends JpaRepository<User, Long> {

//@Repository("matchRepository")
//TODO Cambiar a interface repository cuando implementemos persistencia
@Component("userRepository")
public class UserRepository {
    public Optional<User> findById(String id)
    {
        return GameApplication.getUsers().stream().filter(u -> u.getId().equals(id)).findFirst();
    }
}
