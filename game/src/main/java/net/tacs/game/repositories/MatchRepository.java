package net.tacs.game.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import net.tacs.game.model.Match;

@Repository("matchRepository")
public interface MatchRepository extends MongoRepository<Match, Long> {

	@Query(value="{{users._id::?0}}")
	Match findByUsersId(String userId);

}
