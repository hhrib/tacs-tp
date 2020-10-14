package net.tacs.game.model.interfaces;

import net.minidev.json.annotate.JsonIgnore;
import net.tacs.game.model.MatchConfiguration;

public interface MunicipalityState {
    public String toString();

    public int produceGauchos(MatchConfiguration Config, Double elevation);

    public void createState(Double defenseMultiplier, Double gauchosMultiplier, MunicipalityState nextState);

    public String getName();

    public Double getDefenseMultiplier();

    public MunicipalityState nextState();

    public boolean equals(Object o);
}
