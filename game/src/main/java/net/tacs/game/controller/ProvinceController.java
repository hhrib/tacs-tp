package net.tacs.game.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.services.ProvinceService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ProvinceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Province.class);

	@Autowired
	private ProvinceService provinceService;

	@GetMapping("/provinces")
	public List<Province> getAllProvinces() {
		List<Province> provinces = provinceService.findAll();
		LOGGER.info("provinces qty: {}", provinces.size());
		return provinces;
	}

	@GetMapping("/provinces/{PROVINCE_ID}/municipalities")
	public Municipality[] getMunicipios(@PathVariable("PROVINCE_ID") int provinceId,
			@RequestParam(value = "qty", required = false) Integer qty) {
		return provinceService.findMunicipios(provinceId, qty);
	}

}
