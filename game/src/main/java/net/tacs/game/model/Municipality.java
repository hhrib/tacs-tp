package net.tacs.game.model;

import java.util.Objects;

import net.tacs.game.model.enums.MunicipalityState;

//@Entity
//@Table(name = "municipality")
public class Municipality {

    private Integer id;

    private String nombre;

    private Province province;

    private Centroide centroide;
    
    private Double elevation;

    private MunicipalityState state;

    private User owner;

    private Integer gauchosQty;

	public Municipality() {
		super();
	}

	public Municipality(String nombre) {
	    this.nombre = nombre;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Centroide getCentroide() {
        return centroide;
    }

    public void setCentroide(Centroide centroide) {
        this.centroide = centroide;
    }

	public Double getElevation() {
		return elevation;
	}

	public void setElevation(Double elevation) {
		this.elevation = elevation;
	}

	public MunicipalityState getState() {
		return state;
	}

	public void setState(MunicipalityState state) {
		this.state = state;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

    public Integer getGauchosQty() {
        return gauchosQty;
    }

    public void setGauchosQty(Integer gauchosQty) {
        this.gauchosQty = gauchosQty;
    }

    @Override
    public String toString() {
        return "Municipality{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", province=" + province +
                ", centroide=" + centroide +
                ", elevation=" + elevation +
                ", state=" + state +
                ", owner=" + owner +
                ", gauchosQty=" + gauchosQty +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Municipality municipality = (Municipality) o;
        return Objects.equals(nombre, municipality.nombre);
    }
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
