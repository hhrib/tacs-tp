package net.tacs.game.repositories;

import net.tacs.game.GameApplication;
import net.tacs.game.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository("matchRepository")
//TODO Cambiar a interface repository cuando implementemos persistencia
@Component("matchRepository")
public class MatchRepository {//extends JpaRepository<Match, Long> {

        public Optional<Match> findById(Long id) {
               return GameApplication.getMatches().stream().filter(match -> match.getId().equals(id)).findFirst();
        }

}
