package net.tacs.game.model.interfaces;

import net.tacs.game.model.MatchConfiguration;

public class MunicipalityDefense implements MunicipalityState {
	private final String NAME = "DEFENSE";


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
	public String getName() {
		return NAME;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public MunicipalityState nextState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDefenseMultiplier(MatchConfiguration config) {
		return config.getMultDefense();
	}

}
