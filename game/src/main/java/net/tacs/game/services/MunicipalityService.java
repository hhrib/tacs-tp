package net.tacs.game.services;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.exceptions.MatchNotPlayerTurnException;
import net.tacs.game.exceptions.MatchNotStartedException;
import net.tacs.game.model.*;
import net.tacs.game.model.dto.AttackMuniDTO;
import net.tacs.game.model.dto.AttackResultDTO;
import net.tacs.game.model.dto.MoveGauchosDTO;

import java.util.List;
import java.util.Map;

public interface MunicipalityService {
	/**
	 * 
	 * @param location
	 * @return Elevation for determined latitude and longitude
	 */
	public Double getElevation(Centroide location);

	public Map<Integer, Double> getElevations(List<Municipality> municipalities);

	/**
	 * @method attackMunicipality
	 * @param myMunicipality
	 * @param enemyMunicipality
	 * @param config
	 * @param gauchosAttacking
	 * @return attackResult
	 * @description return the result of the attack between municipalities
	 */
	public AttackResultDTO attackMunicipality(Match match, AttackMuniDTO attackMuniDTO) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException;

	public void produceGauchos(Match match, User user);

	public List<Municipality> moveGauchos(Match match, MoveGauchosDTO requestBean) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException;

}
