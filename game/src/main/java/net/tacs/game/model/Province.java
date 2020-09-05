package net.tacs.game.model;

import java.util.Objects;

public class Province {

	private Integer id;

	private String nombre;

	private Centroide centroide;

	public Province() {
		super();
	}
	
	
	public Province(String nombre) {
		super();
		this.nombre = nombre;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Centroide getCentroide() {
		return centroide;
	}

	public void setCentroide(Centroide centroide) {
		this.centroide = centroide;
	}

	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Province{" + "name='" + nombre + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Province province = (Province) o;
		return Objects.equals(nombre, province.nombre);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}
}
