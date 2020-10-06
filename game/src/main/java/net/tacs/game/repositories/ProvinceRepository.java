package net.tacs.game.repositories;

import net.tacs.game.GameApplication;
import net.tacs.game.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Repository("provinceRepository")
//TODO Cambiar a interface repository cuando implementemos persistencia
@Component("provinceRepository")
public class ProvinceRepository {//extends JpaRepository<Match, Long> {

    private static List<Province> provinces = new ArrayList<>();

    public Optional<Province> findById(Long id) {
        return provinces.stream().filter(province -> province.getId().equals(id)).findFirst();
    }

    public void add(Province newProvince)
    {
        provinces.add(newProvince);
    }

    public static List<Province> getProvinces() {
        return provinces;
    }

    public static void setProvinces(List<Province> provinces) {
        ProvinceRepository.provinces = provinces;
    }
}
