package net.tacs.game.model.interfaces;

import net.tacs.game.model.MatchConfiguration;

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

}
