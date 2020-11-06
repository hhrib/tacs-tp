package net.tacs.game.model.interfaces;

import net.tacs.game.model.MatchConfiguration;
import net.tacs.game.model.dto.DefenseResultDTO;

public class MunicipalityProduction implements MunicipalityState {
	private String name = "PRODUCTION";

	public MunicipalityProduction() {
	}

	@Override
	public int produceGauchos(MatchConfiguration config, Double elevation) {
		return (int) Math.floor(config.getMultGauchosProduction() * (1 - ((elevation - config.getMinHeight())
				/ (config.getMultHeight() * (config.getMaxHeight() - config.getMinHeight())))));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name){
		this.name = name;
	}

	@Override
	public MunicipalityState nextState() {
		return new MunicipalityDefense();
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public double getDefenseMultiplier(MatchConfiguration config) {
		return 1d;
	}

	@Override
	public DefenseResultDTO defend(MatchConfiguration config, Double multDist, Double multAltura, int gauchosAtk, int gauchosDef) {
		double multDefensa = config.getMultDefense();

		int GauchosAtacantesFinal = (int) Math.round(Math.floor(gauchosAtk * multDist -
				gauchosDef * multAltura * multDefensa));

		int GauchosDefensaFinal = (int) Math.round(Math.ceil((gauchosDef * multAltura * multDefensa -
				gauchosAtk * multDist) / (multAltura * multDefensa)));

		if(GauchosAtacantesFinal <= 0)
		{
			//defendido el ataque
			return new DefenseResultDTO(0, GauchosAtacantesFinal, GauchosDefensaFinal);
		} else if(GauchosDefensaFinal <= 0){
			//derrota
			return new DefenseResultDTO(1, GauchosAtacantesFinal, GauchosDefensaFinal);
		} else
			return new DefenseResultDTO(-1, GauchosAtacantesFinal, GauchosDefensaFinal);
	}

}
