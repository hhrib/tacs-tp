package net.tacs.game.model.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.tacs.game.model.MatchConfiguration;

public class MunicipalityDefense implements MunicipalityState {
    private Double defenseMultiplier;
    private Double gauchosMultiplier;
    private MunicipalityState nextState;

    @Override
    public int produceGauchos(MatchConfiguration Config, Double elevation) {
        return (int) Math.floor(gauchosMultiplier *
                (1 - ((elevation - Config.getMinHeight()) /
                        (Config.getMultHeight() * (Config.getMaxHeight() - Config.getMinHeight())))));
    }

    @Override
    public void createState(Double defenseMultiplier, Double gauchosMultiplier, MunicipalityState nextState) {
        this.defenseMultiplier = defenseMultiplier;
        this.gauchosMultiplier = gauchosMultiplier;
        this.nextState = nextState;
    }

    @Override
    public String getName() {
        return "DEFENSE";
    }

    @Override
    @JsonIgnore
    public Double getDefenseMultiplier() {
        return defenseMultiplier;
    }

    @Override
    public MunicipalityState nextState() {
        return nextState;
    }

    @Override
    public String toString() {
        return "DEFENSE";
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
