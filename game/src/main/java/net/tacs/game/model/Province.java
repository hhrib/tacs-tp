package net.tacs.game.model;

import net.tacs.game.exceptions.NotEnoughMunicipalitiesException;

import javax.persistence.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@Entity
//@Table(name = "province")
public class Province {

//    @Id
//    @GeneratedValue
    private Long id;
    private String name;
//    @OneToMany(cascade = {CascadeType.ALL})
    private List<Municipality> municipalities;

    public Province() {
        this.id = new SecureRandom().nextLong();
    }

    //TODO Random para el id es temporal hasta que implementemos persistencia
    public Province(String name) {
        this.id = new SecureRandom().nextLong();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Municipality> getMunicipalities() {
        return municipalities;
    }

    public void setMunicipalities(List<Municipality> municipalities) {
        this.municipalities = municipalities;
    }

    @Override
    public String toString() {
        return "Province{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Province province = (Province) o;
        return Objects.equals(name, province.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
