package net.tacs.game.services;

import net.tacs.game.model.Centroide;

public interface MunicipalityService {
	/**
	 * 
	 * @param location
	 * @return Elevation for determined latitude and longitude
	 */
	public Double getElevation(Centroide location);
}
