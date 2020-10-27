package net.tacs.game.model.interfaces;

import net.tacs.game.model.MatchConfiguration;

public class MunicipalityDefense implements MunicipalityState {
	private String name = "DEFENSE";


	public MunicipalityDefense() {
		super();
	}

	/**
	 * 
	 */
	@Override
	public int produceGauchos(MatchConfiguration config, Double elevation) {
		return (int) Math.floor(config.getMultGauchosDefense()*
                (1 - ((elevation - config.getMinHeight()) /
                        (config.getMultHeight() * (config.getMaxHeight() - config.getMinHeight())))));
		
	}

	@Override
	public void setName(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public MunicipalityState nextState() {
		return new MunicipalityProduction();
	}

	@Override
	public double getDefenseMultiplier(MatchConfiguration config) {
		return config.getMultDefense();
	}

}
