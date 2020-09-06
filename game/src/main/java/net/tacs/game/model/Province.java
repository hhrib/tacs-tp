package net.tacs.game.model;

import java.util.Objects;

public class Province {

	private Integer id;

	private String name;

	private Centroide centroide;

	public Province() {
		super();
	}
	
	
	public Province(String name) {
		super();
		this.name = name;
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

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Province{" + "name='" + name + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Province province = (Province) o;
		return Objects.equals(name, province.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
