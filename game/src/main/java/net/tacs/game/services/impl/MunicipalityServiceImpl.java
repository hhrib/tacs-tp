package net.tacs.game.services.impl;

import java.util.*;
import java.util.stream.Collectors;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.*;
import net.tacs.game.model.dto.MoveGauchosDTO;
import net.tacs.game.model.dto.UpdateMunicipalityStateDTO;
import net.tacs.game.repositories.MatchRepository;
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
    private MatchRepository matchRepository;

	
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
	public int attackMunicipality(Municipality myMunicipality, Municipality enemyMunicipality, MatchConfiguration config, int gauchosAttacking) {
		return myMunicipality.attack(enemyMunicipality, config, gauchosAttacking);
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

    public List<Municipality> moveGauchos(MoveGauchosDTO requestBean) throws MatchException {

        Optional<Match> matchOptional = matchRepository.findById(requestBean.getMatchId());

        Match match = matchOptional.orElseThrow(() -> new MatchException(HttpStatus.BAD_REQUEST, Arrays.asList(new ApiError(MATCH_NOT_FOUND_CODE, MATCH_NOT_FOUND_DETAIL))));

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
