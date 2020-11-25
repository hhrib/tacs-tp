package net.tacs.game.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.tacs.game.model.serializers.ProvinceSerializer;

import java.security.SecureRandom;
import java.util.*;

@JsonSerialize(using = ProvinceSerializer.class)
public class Province {

    private Long id;

	private String nombre;

	private Map<Integer, Municipality> municipalities = new HashMap<>();

	private Centroide centroide;

	public Province() {
        this.id = new SecureRandom().nextLong();
	}
	
	public Province(String nombre) {
        this.id = new SecureRandom().nextLong();
		this.nombre = nombre;
	}


	public Long getId() {
		return id;
	}

	public void setId(long id)
	{
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

	public void setId(Long id) {
		this.id = id;
	}

	public Map<Integer, Municipality> getMunicipalities() {
		return municipalities;
	}

	/*public void setMunicipalityMap(Map<Integer, Municipality> municipalityMap) {
		this.municipalityMap = municipalityMap;
	}*/

	public void setMunicipalities(List<Municipality> municipalities) {
		for (Municipality aMuni : municipalities) {
			this.addMunicipalityMap(aMuni);
		}
	}

	public void addMunicipalityMap(Municipality municipality){
		municipalities.put(municipality.getId(), municipality);
	}

	@Override
	public String toString() {
		return "Province{" + "nombre='" + nombre + '\'' + '}';
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
