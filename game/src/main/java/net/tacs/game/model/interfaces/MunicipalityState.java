package net.tacs.game.model.interfaces;

import net.tacs.game.model.MatchConfiguration;
import net.tacs.game.model.dto.DefenseResultDTO;

public interface MunicipalityState {
    public String toString();

    public int produceGauchos(MatchConfiguration Config, Double elevation);

    public String getName();

    public void setName(String name);

    public MunicipalityState nextState();

    public boolean equals(Object o);

	public double getDefenseMultiplier(MatchConfiguration config);

	public DefenseResultDTO defend(MatchConfiguration config, Double multDist, Double multAltura, int gauchosAtk, int gauchosDef);
}
