package net.tacs.game.repositories;

import net.tacs.game.GameApplication;
import net.tacs.game.model.Match;
import net.tacs.game.model.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository("municipalityRepository")
//TODO Cambiar a interface repository cuando implementemos persistencia
@Component("municipalityRepository")
public class MunicipalityRepository {//extends JpaRepository<Match, Long> {

    public Optional<Municipality> findById(Integer id) {
        return GameApplication.getMunicipalities().stream().filter(muni -> muni.getId().equals(id)).findFirst();
    }

    public int update(Municipality updatedMunicipality) {
        //Esto queda medio raro porque no estamos usando Persistencia, como que estoy escribiendo el mismo objeto en memoria.
        //Pero cuando implementemos BD esto vuela y va a quedar la implementación de mongo correcta.
        Optional<Municipality> oldMunicipalityOptional = GameApplication.getMunicipalities().stream().filter(m -> m.getId().equals(updatedMunicipality.getId())).findFirst();
        //Asumo que siempre existe, el servicio ya validó.
        Municipality oldMuni = oldMunicipalityOptional.get();
        oldMuni = updatedMunicipality;
        return 0; //Terminó ok
    }

}

