package net.tacs.game.repositories;

import net.tacs.game.GameApplication;
import net.tacs.game.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Repository("matchRepository")
//TODO Cambiar a interface repository cuando implementemos persistencia
@Component("matchRepository")
public class MatchRepository {//extends JpaRepository<Match, Long> {

    private static List<Match> matches = new ArrayList<>();

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public Optional<Match> findById(Long id) {
        return matches.stream().filter(match -> match.getId().equals(id)).findFirst();
    }

    public int update(Match updatedMatch) {
        //Esto queda medio raro porque no estamos usando Persistencia, como que estoy escribiendo el mismo objeto en memoria.
        //Pero cuando implementemos BD esto vuela y va a quedar la implementación de mongo correcta.
        Optional<Match> oldMatchOptional = matches.stream().filter(m -> m.getId().equals(updatedMatch.getId())).findFirst();
        //Asumo que siempre existe, el servicio ya validó.
        Match oldMatch = oldMatchOptional.get();
        oldMatch = updatedMatch;
        return 0; //Terminó ok
    }

    public void add(Match newMatch)
    {
        matches.add(newMatch);
    }
}
