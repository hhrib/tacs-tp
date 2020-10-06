package net.tacs.game.repositories;

import net.tacs.game.GameApplication;
import net.tacs.game.model.Match;
import net.tacs.game.model.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Repository("municipalityRepository")
//TODO Cambiar a interface repository cuando implementemos persistencia
@Component("municipalityRepository")
public class MunicipalityRepository {//extends JpaRepository<Match, Long> {
    private static List<Municipality> municipalities = new ArrayList<>();

    public Optional<Municipality> findById(Integer id) {
        return municipalities.stream().filter(muni -> muni.getId().equals(id)).findFirst();
    }
}

