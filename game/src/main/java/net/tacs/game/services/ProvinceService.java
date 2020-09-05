package net.tacs.game.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.tacs.game.model.GeoRefMunicipioAPIResponse;
import net.tacs.game.model.GeoRefProvinceAPIResponse;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.model.opentopodata.ElevationBean;
import net.tacs.game.model.opentopodata.ElevationResponse;

@Service
public class ProvinceService {
	private static final String URL_GEO = "https://api.opentopodata.org/v1/test-dataset?locations=%s,%s";
	private static final String URL_ALL_PROVINCES = "https://apis.datos.gob.ar/georef/api/provincias";
	private static final String URL_MUNICIPIOS = "https://apis.datos.gob.ar/georef/api/municipios?provincia=%s&campos=id,nombre,centroide.lat,centroide.lon";
	private static final String URL_ELEVATION = "https://api.opentopodata.org/v1/srtm90m?locations=";

	/**
	 * 
	 * @return all provinces
	 */
	public List<Province> findAll() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<GeoRefProvinceAPIResponse> response = restTemplate.getForEntity(URL_ALL_PROVINCES,
				GeoRefProvinceAPIResponse.class);
		return Arrays.asList(response.getBody().getProvincias());
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
	 * @param provinceId
	 * @param qty
	 * @return all municipios with elevator
	 */
	@Deprecated
	public Municipality[] findMunicipiosWithElevation(int provinceId, Integer qty) {
		RestTemplate restTemplate = new RestTemplate();

		String url = String.format(URL_MUNICIPIOS, provinceId, qty);
		if (qty != null)
			url += "&max=" + qty;
		ResponseEntity<GeoRefMunicipioAPIResponse> response = restTemplate.getForEntity(url,
				GeoRefMunicipioAPIResponse.class);
		return findElevation(Arrays.asList(response.getBody().getMunicipios()));
	}

	@Deprecated
	private Municipality[] findElevation(List<Municipality> municipalities) {
		Map<String, Municipality> responseMap = new HashMap<>();
		List<Municipality> aux = new LinkedList<Municipality>();
		for (int i = 0; i < municipalities.size(); i++) {
			aux.add(municipalities.get(i));
			if (i % 90 == 0) {
				getElevations(aux, responseMap);
				aux.clear();
			}
		}
		getElevations(aux, responseMap);
		return (Municipality[]) responseMap.values().toArray(new Municipality[responseMap.values().size()]);
	}

	@Deprecated
	private void getElevations(List<Municipality> municipalities, Map<String, Municipality> responseMap) {
		RestTemplate restTemplate = new RestTemplate();
		StringBuilder sb = new StringBuilder();
		for (Municipality m : municipalities) {
			sb.append(m.getCentroide().getLat().concat(",").concat(m.getCentroide().getLon()).concat("|"));
			responseMap.put(m.getCentroide().getLat().concat(",").concat(m.getCentroide().getLon()), m);
		}
		ResponseEntity<ElevationResponse> response = restTemplate.getForEntity(
				URL_ELEVATION.concat(sb.toString()).concat("&interpolation=cubic"), ElevationResponse.class);
		for (ElevationBean elevation : response.getBody().getResults()) {
			Municipality m = responseMap
					.get(elevation.getLocation().getLat().concat(",").concat(elevation.getLocation().getLng()));
			m.setElevation(elevation.getElevation());
		}
	}

	/**
	 * 
	 * @param lat
	 * @param lon
	 * @return Elvation for determined latitude and longitude
	 */
	public Double getElevation(String lat, String lon) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ElevationResponse> response = restTemplate.getForEntity(
				URL_ELEVATION.concat(lat).concat(",").concat(lon).concat("&interpolation=cubic"),
				ElevationResponse.class);
		return response.getBody().getResults()[0].getElevation();
	}
}
