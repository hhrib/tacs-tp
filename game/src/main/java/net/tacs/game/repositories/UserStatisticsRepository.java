package net.tacs.game.repositories;

import net.tacs.game.model.UserStats;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("userStatisticsRepository")
public interface UserStatisticsRepository extends MongoRepository<UserStats, String> {

    @Query(value = "{{usersStats._id::?0}}")
    UserStats getById(String id);
}
