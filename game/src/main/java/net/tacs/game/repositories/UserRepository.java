package net.tacs.game.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.tacs.game.model.User;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, String> {
}
