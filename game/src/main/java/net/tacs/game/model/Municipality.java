package net.tacs.game.model;

import java.util.Arrays;
import java.util.Objects;

//import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.interfaces.MunicipalityDefense;
import net.tacs.game.model.interfaces.MunicipalityState;
import org.springframework.http.HttpStatus;

import static net.tacs.game.constants.Constants.*;
import static net.tacs.game.constants.Constants.NOT_ENOUGH_GAUCHOS_DETAIL;

//@Entity
//@Table(name = "municipality")
public class Municipality {
    private static Integer idCounter = 0;

    private Integer id;

    private String nombre;

    private Centroide centroide;
    
    private Double elevation;
//    @Transient
    private MunicipalityState state;
//    @Transient
//    private final MunicipalityProduction productionState = new MunicipalityProduction();
//    @Transient
//    private final MunicipalityDefense defenseState = new MunicipalityDefense();

    private User owner;

    private Integer gauchosQty;

    private boolean blocked = false;

	public Municipality() {
		super();
		id = ++idCounter;
		state = new MunicipalityDefense();
	}

    public Municipality(Double defenseMultiplier, Double gauchosProdMultiplier, Double gauchosDefMultiplier) {
        super();
        id = ++idCounter;
//        defenseState.createState(defenseMultiplier, gauchosDefMultiplier, productionState);
//        productionState.createState(1D, gauchosProdMultiplier, defenseState);
        state = new MunicipalityDefense();
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

	public void nextState()
    {
        state = state.nextState();
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

    public void addGauchos(int Quantity){
	    this.gauchosQty += Quantity;

	    if(gauchosQty < 0)
	        gauchosQty = 0;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void validateMoveGauchos(Municipality muniDestiny, Integer qty) throws MatchException {
        if (!this.owner.equals(muniDestiny.getOwner())) {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(PLAYER_DOESNT_OWN_MUNIS_CODE, PLAYER_DOESNT_OWN_MUNIS_DETAIL)));
        }
        if (this.gauchosQty < qty) {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(NOT_ENOUGH_GAUCHOS_CODE, NOT_ENOUGH_GAUCHOS_DETAIL)));
        }
    }

    public void validateReceiveGauchos() throws MatchException {
        if(this.isBlocked()) {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MUNICIPALITY_DESTINY_BLOCKED_CODE, MUNICIPALITY_DESTINY_BLOCKED_DETAIL)));
        }
    }

    public void validateAttack(Municipality muniDef, Integer attackGauchosQty) throws MatchException {
        if (this.isBlocked()) {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MUNICIPALITY_DESTINY_BLOCKED_CODE, MUNICIPALITY_DESTINY_BLOCKED_DETAIL)));
        }
        if (this.owner.equals(muniDef.getOwner())) {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(SAME_OWNER_MUNIS_CODE, SAME_OWNER_MUNIS_DETAIL)));
        }
        if (this.gauchosQty < attackGauchosQty) {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(NOT_ENOUGH_GAUCHOS_CODE, NOT_ENOUGH_GAUCHOS_DETAIL)));
        }
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
     * @method produceGauchos
     * @param Config
     * @description se ejecuta una vez por turno del jugador, aumenta la cantidad de gauchos en el municipio
     * en base al estado
     */
    public void produceGauchos(MatchConfiguration Config)
    {
        gauchosQty += state.produceGauchos(Config, elevation);
    }

    /**
     * @method attack
     * @param enemyMunicipality
     * @param config
     * @return 1  --  attack successful
     *         0  --  attack repelled
     *        -1  --  attack incomplete?
     */
    public int attack(Municipality enemyMunicipality, MatchConfiguration config, int GauchosAttacking) {
	    double distanciaEntreMunicipios = centroide.getDistance(enemyMunicipality.getCentroide());

        double multDist = 1 - (distanciaEntreMunicipios - config.getMinDist()) /
                                (config.getMultDistance() * (config.getMaxDist() - config.getMinDist()));

        if(Double.isNaN(multDist))
            multDist = 1;

        double multAltura = (1 + (enemyMunicipality.getElevation() - config.getMinHeight()) /
                                (config.getMultHeight() * (config.getMaxHeight() - config.getMinHeight())));

        double multDefensa = enemyMunicipality.getState().getDefenseMultiplier(config);

	    int GauchosAtacantesFinal = (int) Math.round(Math.floor(GauchosAttacking * multDist -
                enemyMunicipality.getGauchosQty() * multAltura * multDefensa));

	    int GauchosDefensaFinal = (int) Math.round(Math.ceil((enemyMunicipality.getGauchosQty() * multAltura * multDefensa -
                GauchosAttacking * multDist) / (multAltura * multDefensa)));

        this.setBlocked(true);

	    if(GauchosAtacantesFinal <= 0)
        {
            //falló el ataque
            enemyMunicipality.setGauchosQty(GauchosDefensaFinal);
            setGauchosQty(getGauchosQty() - GauchosAttacking);
            return 0;
        } else {
            //ataque victorioso
            enemyMunicipality.setGauchosQty(GauchosAtacantesFinal);
            enemyMunicipality.setOwner(getOwner());
            setGauchosQty(getGauchosQty() - GauchosAttacking);
            return 1;
        }

    }

}
