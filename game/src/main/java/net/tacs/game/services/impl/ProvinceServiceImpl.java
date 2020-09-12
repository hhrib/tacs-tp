package net.tacs.game.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.tacs.game.model.Centroide;
import net.tacs.game.model.GeoRefMunicipioAPIResponse;
import net.tacs.game.model.GeoRefProvinceAPIResponse;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.model.opentopodata.ElevationResponse;
import net.tacs.game.services.ProvinceService;

@Service("provinceService")
public class ProvinceServiceImpl implements ProvinceService{
	private static final String URL_ALL_PROVINCES = "https://apis.datos.gob.ar/georef/api/provincias";
	private static final String URL_MUNICIPIOS = "https://apis.datos.gob.ar/georef/api/municipios?provincia=%s&campos=id,nombre,centroide.lat,centroide.lon";
	private static final String URL_ELEVATION = "https://api.opentopodata.org/v1/srtm90m?locations=";

	private Map<Centroide, Double> elevations = new HashMap<>();
	private List<Province> provinces;

	/**
	 * 
	 * @return all provinces
	 */
	public List<Province> findAll() {
		RestTemplate restTemplate = new RestTemplate();
		if (provinces.isEmpty()) {
			ResponseEntity<GeoRefProvinceAPIResponse> response = restTemplate.getForEntity(URL_ALL_PROVINCES,
					GeoRefProvinceAPIResponse.class);
			provinces.addAll(Arrays.asList(response.getBody().getProvincias()));
		}
		return provinces;
	}

	/**
	 * 
	 * @param provinceId
	 * @param qty
	 * @return all provinces for a determined province
	 */
	public Municipality[] findMunicipios(int provinceId, Integer qty) {
		RestTemplate restTemplate = new RestTemplate();

		String url = String.format(URL_MUNICIPIOS, provinceId, qty);
		if (qty != null)
			url += "&max=" + qty;
		ResponseEntity<GeoRefMunicipioAPIResponse> response = restTemplate.getForEntity(url,
				GeoRefMunicipioAPIResponse.class);
		return response.getBody().getMunicipios();
	}

	/**
	 * 
	 * @param location
	 * @return Elvation for determined latitude and longitude
	 */
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
}
