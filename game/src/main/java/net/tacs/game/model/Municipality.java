package net.tacs.game.model;

import net.tacs.game.model.enums.MunicipalityState;

import javax.persistence.*;
import java.util.Objects;

//@Entity
//@Table(name = "municipality")
public class Municipality {

    public Municipality() {
    }

    public Municipality(String name) {
        this.name = name;
    }

    @Id //@GeneratedValue
    private Long id;

    private String name;
//    @ManyToOne
    private Province province;
//    @ManyToOne
    private User owner;

    private MunicipalityState state;

    private Integer gauchosQty;

    private Double elevation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public MunicipalityState getState() {
        return state;
    }

    public void setState(MunicipalityState state) {
        this.state = state;
    }

    public Integer getGauchosQty() {
        return gauchosQty;
    }

    public void setGauchosQty(Integer gauchosQty) {
        this.gauchosQty = gauchosQty;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }


    @Override
    public String toString() {
        return "Municipality{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Municipality municipality = (Municipality) o;
        return Objects.equals(name, municipality.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
