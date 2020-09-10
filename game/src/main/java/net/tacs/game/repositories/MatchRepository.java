package net.tacs.game.repositories;

import net.tacs.game.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository("matchRepository")
public interface MatchRepository {//extends JpaRepository<Match, Long> {
        List<Match> findAll();
}
