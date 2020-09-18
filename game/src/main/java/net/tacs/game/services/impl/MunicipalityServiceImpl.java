package net.tacs.game.services.impl;

import java.util.HashMap;
import java.util.Map;

import net.tacs.game.model.MatchConfiguration;
import net.tacs.game.model.Municipality;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.tacs.game.model.Centroide;
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
}
