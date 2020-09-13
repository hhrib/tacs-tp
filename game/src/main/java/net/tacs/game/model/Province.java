package net.tacs.game.model;


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

	private String nombre;
//    @OneToMany(cascade = {CascadeType.ALL})
    private List<Municipality> municipalities = new ArrayList<>();

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

	public List<Municipality> getMunicipalities() {
        return municipalities;
    }

    public void setMunicipalities(List<Municipality> municipalities) {
        this.municipalities = municipalities;
	}

	public void addMunicipality(Municipality municipality){ this.municipalities.add(municipality);}

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
