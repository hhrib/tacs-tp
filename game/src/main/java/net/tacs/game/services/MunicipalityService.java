package net.tacs.game.services;

import net.tacs.game.model.Centroide;
import net.tacs.game.model.MatchConfiguration;
import net.tacs.game.model.Municipality;
import org.springframework.http.ResponseEntity;

public interface MunicipalityService {
	/**
	 * 
	 * @param location
	 * @return Elevation for determined latitude and longitude
	 */
	public Double getElevation(Centroide location);

	/**
	 * @method attackMunicipality
	 * @param myMunicipality
	 * @param enemyMunicipality
	 * @param config
	 * @param gauchosAttacking
	 * @return attackResult
	 * @description return the result of the attack between municipalities
	 */
	public int attackMunicipality(Municipality myMunicipality, Municipality enemyMunicipality, MatchConfiguration config, int gauchosAttacking);
}
