package net.tacs.game.services.impl;

import java.util.HashMap;
import java.util.Map;

import net.tacs.game.model.*;
import net.tacs.game.model.enums.MunicipalityState;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.tacs.game.model.opentopodata.ElevationResponse;
import net.tacs.game.services.MunicipalityService;

@Service("municipalityService")
public class MunicipalityServiceImpl implements MunicipalityService {
	private static final String URL_ELEVATION = "https://api.opentopodata.org/v1/srtm90m?locations=";
	/**
	 * 
	 * @param location
	 * @return Elevation for determined latitude and longitude
	 */
	private Map<Centroide, Double> elevations = new HashMap<>();
	
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

	public int attackMunicipality(Municipality myMunicipality, Municipality enemyMunicipality, MatchConfiguration config, int gauchosAttacking) {
		return myMunicipality.attack(enemyMunicipality, config, gauchosAttacking);
	}

	public void changeState(Municipality myMunicipality, MunicipalityState newState) {
		myMunicipality.setState(newState);
	}

	public void produceGauchos(Match match, User user) {
		for (Municipality municipality : match.getMap().getMunicipalities()) {
			if(municipality.getOwner().equals(user))
			{
				municipality.produceGauchos(match.getConfig());
			}
		}
	}

    public void moveGauchos(Match match, long IdOrigin, long IdDestiny, int Qty)
    {
        if(IdOrigin == IdDestiny)
        {
            //TODO mover gauchos entre el mismo municipio no se permite
            return;
        }

        Municipality muniOrigin = null;
        Municipality muniDestiny = null;

        boolean bOriginFound = false;
        boolean bDestinyFound = false;

        for(Municipality aMuni : match.getMap().getMunicipalities())
        {
            if(aMuni.getId() == IdOrigin)
            {
                muniOrigin = aMuni;
                bOriginFound = true;
            }
            if(aMuni.getId() == IdDestiny)
            {
                muniDestiny = aMuni;
                bDestinyFound = true;
            }
        }

        if(bOriginFound && bDestinyFound)
        {
            if(muniOrigin.getGauchosQty() < Qty)
            {
                //TODO municipalidad no tiene tantos gauchos
                return;
            }

            muniOrigin.addGauchos(-Qty);
            muniDestiny.addGauchos(Qty);
        }
        else
        {
            //TODO no se encontraron los municipios
            return;
        }

    }
}
