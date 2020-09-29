package net.tacs.game.repositories;

import net.tacs.game.GameApplication;
import net.tacs.game.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository("provinceRepository")
//TODO Cambiar a interface repository cuando implementemos persistencia
@Component("provinceRepository")
public class ProvinceRepository {//extends JpaRepository<Match, Long> {

    public Optional<Province> findById(Long id) {
        return GameApplication.getProvinces().stream().filter(province -> province.getId().equals(id)).findFirst();
    }

    /*public int update(Province updatedProvince) {
        //Esto queda medio raro porque no estamos usando Persistencia, como que estoy escribiendo el mismo objeto en memoria.
        //Pero cuando implementemos BD esto vuela y va a quedar la implementación de mongo correcta.
        Optional<Province> oldProvinceOptional = GameApplication.getProvinces().stream().filter(p -> p.getId().equals(updatedProvince.getId())).findFirst();
        //Asumo que siempre existe, el servicio ya validó.
        Province oldProvince = oldProvinceOptional.get();
        oldProvince = updatedProvince;
        return 0; //Terminó ok
    }*/

}
