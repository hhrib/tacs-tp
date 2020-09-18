package net.tacs.game.model;

import java.util.Objects;

import net.tacs.game.model.enums.MunicipalityState;

//@Entity
//@Table(name = "municipality")
public class Municipality {

    private Integer id;

    private String nombre;

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

    /**
     * @method attack
     * @param enemyMunicipality
     * @param Config
     * @return 1  --  attack successful
     *         0  --  attack repelled
     *        -1  --  attack incomplete?
     */
    public int attack(Municipality enemyMunicipality, MatchConfiguration Config, int GauchosAttacking) {
	    double distanciaEntreMunicipios = centroide.getDistance(enemyMunicipality.getCentroide());

        double multDist = 1 - (distanciaEntreMunicipios - Config.getMinDist()) / (2 *(Config.getMaxDist() - Config.getMinDist()));

        double multAltura = (1 + (enemyMunicipality.getElevation() - Config.getMinHeight()) / (2 * (Config.getMaxHeight() - Config.getMinHeight())));

        double multDefensa = 1;
        if(enemyMunicipality.getState() == MunicipalityState.DEFENSE)
        {
            multDefensa = Config.getMultDefense();
        }

	    int GauchosAtacantesFinal = (int) Math.round(Math.floor(GauchosAttacking * multDist -
                enemyMunicipality.getGauchosQty() * multAltura * multDefensa));

	    int GauchosDefensaFinal = (int) Math.round(Math.ceil((enemyMunicipality.getGauchosQty() * multAltura * multDefensa -
                GauchosAttacking * multDist) / (multAltura * multDefensa)));

	    if(GauchosAtacantesFinal <= 0)
        {
            //TODO falló el ataque
            enemyMunicipality.setGauchosQty(GauchosDefensaFinal);
            setGauchosQty(getGauchosQty() - GauchosAttacking);
            return 0;
        }

	    if(GauchosAtacantesFinal > 0 && GauchosDefensaFinal <= 0)
        {
            //TODO ataque victorioso
            enemyMunicipality.setGauchosQty(GauchosAtacantesFinal);
            enemyMunicipality.setOwner(getOwner());
            setGauchosQty(getGauchosQty() - GauchosAttacking);
            return 1;
        }

	    if(GauchosAtacantesFinal > 0 && GauchosDefensaFinal > 0)
        {
            //TODO preguntar que pasa
            return -1;
        }

	    return -2; //No deberia llegar nunca aqui
    }
}
