package net.tacs.game.services.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.tacs.game.model.GeoRefMunicipioAPIResponse;
import net.tacs.game.model.GeoRefProvinceAPIResponse;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.services.ProvinceService;

@Service("provinceService")
public class ProvinceServiceImpl implements ProvinceService {
	private static final String URL_ALL_PROVINCES = "https://apis.datos.gob.ar/georef/api/provincias";
	private static final String URL_MUNICIPIOS = "https://apis.datos.gob.ar/georef/api/municipios?provincia=%s&campos=id,nombre,centroide.lat,centroide.lon";

	private List<Province> provinces = new LinkedList<>();

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

}
