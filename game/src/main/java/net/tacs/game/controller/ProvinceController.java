package net.tacs.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.tacs.game.model.Centroide;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.services.ProvinceService;

@RestController
public class ProvinceController {

	@Autowired
	private ProvinceService provinceService;

	@GetMapping("/provinces")
	public List<Province> findAll() {
		return provinceService.findAll();
	}

	@GetMapping("/provinces/{PROVINCE_ID}/municipalities")
	public Municipality[] getMuni(@PathVariable("PROVINCE_ID") int provinceId,
			@RequestParam(value = "qty", required = false) Integer qty) {
		return provinceService.findMunicipios(provinceId, qty);
	}

	@GetMapping("/elevation/{LAT}/{LON}")
	public Double getElevation(@PathVariable("LAT") String lat, @PathVariable("LON") String lon) {
		return provinceService.getElevation(new Centroide(lat, lon));
	}
}
