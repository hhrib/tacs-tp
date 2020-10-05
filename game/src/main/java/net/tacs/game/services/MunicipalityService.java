package net.tacs.game.services;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.exceptions.MatchNotPlayerTurnException;
import net.tacs.game.exceptions.MatchNotStartedException;
import net.tacs.game.model.*;
import net.tacs.game.model.dto.AttackMuniDTO;
import net.tacs.game.model.dto.AttackResultDTO;
import net.tacs.game.model.dto.MoveGauchosDTO;
import net.tacs.game.model.dto.UpdateMunicipalityStateDTO;

import java.util.List;

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
	public AttackResultDTO attackMunicipality(String matchId, AttackMuniDTO attackMuniDTO) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException;

	public void produceGauchos(Match match, User user);

	public List<Municipality> moveGauchos(String matchId, MoveGauchosDTO requestBean) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException;

}
