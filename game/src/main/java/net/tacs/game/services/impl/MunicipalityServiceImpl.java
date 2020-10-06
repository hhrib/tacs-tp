package net.tacs.game.services.impl;

import java.util.*;
import java.util.stream.Collectors;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.exceptions.MatchNotPlayerTurnException;
import net.tacs.game.exceptions.MatchNotStartedException;
import net.tacs.game.model.*;
import net.tacs.game.model.dto.AttackMuniDTO;
import net.tacs.game.model.dto.AttackResultDTO;
import net.tacs.game.model.dto.MoveGauchosDTO;
import net.tacs.game.model.dto.UpdateMunicipalityStateDTO;
import net.tacs.game.repositories.MatchRepository;
import net.tacs.game.repositories.MunicipalityRepository;
import net.tacs.game.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.tacs.game.model.opentopodata.ElevationResponse;
import net.tacs.game.services.MunicipalityService;

import static net.tacs.game.constants.Constants.*;

@Service("municipalityService")
public class MunicipalityServiceImpl implements MunicipalityService {
	private static final String URL_ELEVATION = "https://api.opentopodata.org/v1/srtm90m?locations=";
	/**
	 * 
	 * @param location
	 * @return Elevation for determined latitude and longitude
	 */
	private Map<Centroide, Double> elevations = new HashMap<>();

	@Autowired
    private MatchService matchService;

	@Autowired
    private MunicipalityRepository municipalityRepository;
	
	public synchronized Double getElevation(Centroide location) {
		Double elevation = elevations.get(location);
		if (elevation == null) {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<ElevationResponse> response = restTemplate.getForEntity(
					URL_ELEVATION.concat(location.toString()).concat("&interpolation=cubic"), ElevationResponse.class);
			elevation = response.getBody().getResults()[0].getElevation();
		}

		return elevation;
	}

	@Override
	public AttackResultDTO attackMunicipality(String matchId, AttackMuniDTO attackMuniDTO) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = matchService.getMatchById(matchId);

        matchService.CheckMatchNotStarted(match);
        matchService.CheckMatchFinished(match);

        if(attackMuniDTO.getMuniAttackingId() == (attackMuniDTO.getMuniDefendingId()))
        {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(SAME_ORIGIN_DESTINY_CODE, SAME_ORIGIN_DESTINY_DETAIL)));
        }

	    boolean bMuniAttackFound = false;
        boolean bMuniDefenseFound = false;

        Municipality muniAtk = null;
        Municipality muniDef = null;
        User rival = null;

        int result = -2;

	    for(Municipality aMuni : match.getMap().getMunicipalities())
        {
            if(attackMuniDTO.getMuniAttackingId() == aMuni.getId())
            {
                muniAtk = aMuni;
                bMuniAttackFound = true;
            }
            if(attackMuniDTO.getMuniDefendingId() == aMuni.getId())
            {
                muniDef = aMuni;
                rival = aMuni.getOwner();
                bMuniDefenseFound = true;
            }
        }

	    if(bMuniAttackFound && bMuniDefenseFound)
	    {
            if(!match.playerCanAttack(muniAtk.getOwner()))
                throw new MatchNotPlayerTurnException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(PLAYER_DOESNT_HAVE_TURN_CODE, PLAYER_DOESNT_HAVE_TURN_DETAIL)));

            if(muniAtk.isBlocked())
            {
                throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MUNICIPALITY_DESTINY_BLOCKED_CODE, MUNICIPALITY_DESTINY_BLOCKED_DETAIL)));
            }

            if(muniAtk.getOwner().equals(muniDef.getOwner()))
                throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(SAME_OWNER_MUNIS_CODE, SAME_OWNER_MUNIS_DETAIL)));

            result = muniAtk.attack(muniDef, match.getConfig(), attackMuniDTO.getGauchosQty());

            muniAtk.setBlocked(true);

            if(result == 1) //si el rival perdio el municipio chequear si perdio la partida
                match.checkVictory(rival);

            return new AttackResultDTO(result, muniAtk, muniDef);
	    }
	    else
        {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MUNICIPALITY_NOT_FOUND_CODE, MUNICIPALITY_NOT_FOUND_DETAIL)));
        }
	}

	@Override
	public void produceGauchos(Match match, User user) {
		for (Municipality municipality : match.getMap().getMunicipalities()) {
			if(municipality.getOwner().equals(user))
			{
			    //Desbloquear municipio y producir gauchos
                municipality.setBlocked(false);
				municipality.produceGauchos(match.getConfig());
			}
		}
	}

    public List<Municipality> moveGauchos(String matchId, MoveGauchosDTO requestBean) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = matchService.getMatchById(matchId);

        matchService.CheckMatchNotStarted(match);
        matchService.CheckMatchFinished(match);

        if(requestBean.getIdOriginMuni().equals(requestBean.getIdDestinyMuni()))
        {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(SAME_ORIGIN_DESTINY_CODE, SAME_ORIGIN_DESTINY_DETAIL)));
        }

        Municipality muniOrigin = null;
        Municipality muniDestiny = null;

        List<Integer> idsNotFound = new ArrayList<>();
        idsNotFound.add(0, requestBean.getIdOriginMuni());
        idsNotFound.add(1, requestBean.getIdDestinyMuni());

        for (Municipality aMuni : match.getMap().getMunicipalities())
        {
            if (aMuni.getId().equals(requestBean.getIdOriginMuni()))
            {
                muniOrigin = aMuni;
                idsNotFound.removeIf(id -> id.equals(aMuni.getId()));
            }
            if (aMuni.getId().equals(requestBean.getIdDestinyMuni()))
            {
                muniDestiny = aMuni;
                idsNotFound.removeIf(id -> id.equals(aMuni.getId()));
            }
        }

        if(idsNotFound.isEmpty())
        {
            if(!match.playerCanAttack(muniOrigin.getOwner()))
                throw new MatchNotPlayerTurnException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(PLAYER_DOESNT_HAVE_TURN_CODE, PLAYER_DOESNT_HAVE_TURN_DETAIL)));

            if(!muniOrigin.getOwner().equals(muniDestiny.getOwner()))
                throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(PLAYER_DOESNT_OWN_MUNIS_CODE, PLAYER_DOESNT_OWN_MUNIS_DETAIL)));

            if(muniDestiny.isBlocked())
            {
                throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MUNICIPALITY_DESTINY_BLOCKED_CODE, MUNICIPALITY_DESTINY_BLOCKED_DETAIL)));
            }

            if(muniOrigin.getGauchosQty() < requestBean.getQty())
            {
                throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(NOT_ENOUGH_GAUCHOS_CODE, NOT_ENOUGH_GAUCHOS_DETAIL)));
            }

            muniOrigin.addGauchos(-requestBean.getQty());

            //El municipio destino se bloquea
            muniDestiny.addGauchos(requestBean.getQty());
            muniDestiny.setBlocked(true);
        }
        else
        {
            //No se encontró por lo menos alguno de los ids
            String idsNotFoundCommaSeparated = idsNotFound.stream().map(id -> id.toString()).collect(Collectors.joining(","));
            throw new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MUNI_NOT_FOUND_CODE, String.format(MUNI_NOT_FOUND_DETAIL, idsNotFoundCommaSeparated))));
        }

        return Arrays.asList(muniOrigin, muniDestiny);
    }
}
