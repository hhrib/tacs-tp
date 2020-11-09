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
import net.tacs.game.model.dto.PlayerDefeatedDTO;
import net.tacs.game.repositories.MatchRepository;
import net.tacs.game.repositories.MunicipalityRepository;
import net.tacs.game.services.MatchService;
import net.tacs.game.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private MatchRepository matchRepository;

	@Autowired
    private UserService userService;

	@Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private SimpMessagingTemplate template;
	
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

    public synchronized Map<Integer, Double> getElevations(List<Municipality> municipalities) {
	    Map<Integer, Double> elevationsResponse = new HashMap<>();
	    List<Municipality> munisWithoutElevation = new ArrayList<>();
	    String elevationsQuery = "";

        for(Municipality aMuni : municipalities)
        {
            Double elevation = elevations.get(aMuni.getCentroide());

            if (elevation == null)
            {
                munisWithoutElevation.add(aMuni);
                elevationsQuery = elevationsQuery.concat(aMuni.getCentroide().toString() + "|");
            } else {
                elevationsResponse.put(aMuni.getId(), elevation);
            }
        }

        if(elevationsQuery.equals(""))
            return elevationsResponse;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ElevationResponse> response = restTemplate.getForEntity(
                URL_ELEVATION.concat(elevationsQuery).concat("&interpolation=cubic"), ElevationResponse.class);

        for(int i = 0; i < munisWithoutElevation.size(); i++)
        {
            elevationsResponse.put(munisWithoutElevation.get(i).getId(), response.getBody().getResults()[i].getElevation());
        }

        return elevationsResponse;
    }

	@Override
	public AttackResultDTO attackMunicipality(Match match, AttackMuniDTO attackMuniDTO) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
	    match.checkMatchNotStarted();
	    match.checkMatchFinished();

        if(attackMuniDTO.getMuniAttackingId() == (attackMuniDTO.getMuniDefendingId())) {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(SAME_ORIGIN_DESTINY_CODE, SAME_ORIGIN_DESTINY_DETAIL)));
        }

        Municipality muniAtk = match.getMap().getMunicipalities().get(attackMuniDTO.getMuniAttackingId());
        Municipality muniDef = match.getMap().getMunicipalities().get(attackMuniDTO.getMuniDefendingId());

        if(muniAtk == null || muniDef == null) {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MUNICIPALITY_NOT_FOUND_CODE, MUNICIPALITY_NOT_FOUND_DETAIL)));
        }

        match.validatePlayerHasTurn(muniAtk.getOwner());
        muniAtk.validateAttack(muniDef, attackMuniDTO.getGauchosQty());

        User rival = muniDef.getOwner();

        int result = muniAtk.attack(muniDef, match.getConfig(), attackMuniDTO.getGauchosQty());

        if (match.rivalDefeated(rival)) { //si el rival perdio el municipio chequear si perdio la partida
            PlayerDefeatedDTO playerDefeatedSocketMessage = new PlayerDefeatedDTO();
            playerDefeatedSocketMessage.setUsername(rival.getUsername());
            template.convertAndSend("/topic/" + match.getId().toString() +"/defeated_player", playerDefeatedSocketMessage);
        }

        if(match.checkVictory()) {
            userService.setWinnerAndLosersStats(match);
        }

        matchRepository.save(match);
        return new AttackResultDTO(result, muniAtk, muniDef);
	}

	@Override
	public void produceGauchos(Match match, User user) {
		for (Map.Entry<Integer, Municipality> municipality : match.getMap().getMunicipalities().entrySet()) {
			if(municipality.getValue().getOwner().equals(user))
			{
			    //Desbloquear municipio y producir gauchos
                municipality.getValue().setBlocked(false);
				municipality.getValue().produceGauchos(match.getConfig());
			}
		}

        matchRepository.save(match);
	}

    public List<Municipality> moveGauchos(Match match, MoveGauchosDTO requestBean) throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {

	    match.checkMatchNotStarted();
	    match.checkMatchFinished();

        List<ApiError> errors = new ArrayList<>();

        if (requestBean.getIdOriginMuni() == null) {
            errors.add(new ApiError("MUNICIPALITY_ORIGIN_EMPTY", "Origin Municipality not selected"));
        }
        if (requestBean.getIdDestinyMuni() == null) {
            errors.add(new ApiError("MUNICIPALITY_DESTINY_EMPTY", "Destiny Municipality not selected"));
        }
        if (requestBean.getQty() == null || requestBean.getQty() == 0) {
            errors.add(new ApiError("GAUCHOS_QUANTITY_EMPTY", "Quantity of gauchos not selected"));
        }
        if (!errors.isEmpty()) {
            throw new MatchException(HttpStatus.BAD_REQUEST, errors);
        }

        if(requestBean.getIdOriginMuni().equals(requestBean.getIdDestinyMuni()))
        {
            throw new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(SAME_ORIGIN_DESTINY_CODE, SAME_ORIGIN_DESTINY_DETAIL)));
        }

        List<Integer> idsNotFound = new ArrayList<>();

        Municipality muniOrigin = match.getMap().getMunicipalities().get(requestBean.getIdOriginMuni());
        if(muniOrigin == null)
            idsNotFound.add(0, requestBean.getIdOriginMuni());

        Municipality muniDestiny = match.getMap().getMunicipalities().get(requestBean.getIdDestinyMuni());
        if(muniDestiny == null)
            idsNotFound.add(1, requestBean.getIdDestinyMuni());

        //No se encontró por lo menos alguno de los ids
        if(!idsNotFound.isEmpty()) {
            String idsNotFoundCommaSeparated = idsNotFound.stream().map(id -> id.toString()).collect(Collectors.joining(","));
            throw new MatchException(HttpStatus.NOT_FOUND, Arrays.asList(new ApiError(MUNI_NOT_FOUND_CODE, String.format(MUNI_NOT_FOUND_DETAIL, idsNotFoundCommaSeparated))));
        }

        match.validatePlayerHasTurn(muniOrigin.getOwner());
        muniOrigin.validateMoveGauchos(muniDestiny, requestBean.getQty());
        muniDestiny.validateReceiveGauchos();

        muniOrigin.addGauchos(-requestBean.getQty());

        //El municipio destino se bloquea
        muniDestiny.addGauchos(requestBean.getQty());
        muniDestiny.setBlocked(true);

		matchRepository.save(match);

		return Arrays.asList(muniOrigin, muniDestiny);
    }
}
