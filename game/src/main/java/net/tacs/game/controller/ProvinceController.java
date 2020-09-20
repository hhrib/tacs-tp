package net.tacs.game.controller;

import java.util.List;

import net.tacs.game.mapper.ProvinceToBeanMapper;
import net.tacs.game.model.bean.ProvinceBeanResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.services.ProvinceService;

import static net.tacs.game.GameApplication.getProvinces;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ProvinceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Province.class);

	@Autowired
	private ProvinceService provinceService;

	@GetMapping("/provinces")
	public ResponseEntity<List<ProvinceBeanResponse>> getAllProvinces() {

		//List<Province> provinces = provinceService.findAll(); <-- se carga al iniciar la aplicacion
		List<Province> provinces = getProvinces();
		LOGGER.info("provinces qty: {}", provinces.size());
		return new ResponseEntity<>(ProvinceToBeanMapper.mapProvinces(provinces), HttpStatus.OK);
	}

	@GetMapping("/provinces/{PROVINCE_ID}/municipalities")
	public ResponseEntity<List<Municipality>> getMunicipios(@PathVariable("PROVINCE_ID") int provinceId,
			@RequestParam(value = "qty", required = false) Integer qty) {
		return new ResponseEntity<>(provinceService.findMunicipios(provinceId, qty), HttpStatus.OK);
	}

}
