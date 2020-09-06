package net.tacs.game.model;

import net.tacs.game.model.enums.MunicipalityStatus;

public class Municipality {

    private Integer id;

    private String nombre;

    private Centroide centroide;
    
    private Double elevation;

    private MunicipalityStatus status;

    private User owner;

    private Integer gauchosQty;

	public Municipality() {
		super();
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

	public Double getElevation() {
		return elevation;
	}

	public void setElevation(Double elevation) {
		this.elevation = elevation;
	}

	public MunicipalityStatus getStatus() {
		return status;
	}

	public void setStatus(MunicipalityStatus status) {
		this.status = status;
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

	public Centroide getCentroide() {
		return centroide;
	}

	public void setCentroide(Centroide centroide) {
		this.centroide = centroide;
	}

}
