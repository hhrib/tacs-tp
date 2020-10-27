package net.tacs.game.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import net.tacs.game.model.User;

@Repository("userRepository")
public interface UserRepository extends MongoRepository<User, String> {

	@Query(value = "{{users._id::?0}}")
	User findByUsername(String username);

}
