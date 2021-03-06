package net.tacs.game.controller;

import java.util.List;

import net.tacs.game.mapper.ProvinceToDTOMapper;
import net.tacs.game.model.dto.ProvinceDTOResponse;
import net.tacs.game.repositories.ProvinceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.services.ProvinceService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://tacswololo.tk"})
public class ProvinceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Province.class);

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private ProvinceRepository provinceRepository;

	@GetMapping("/provinces")
	public ResponseEntity<List<ProvinceDTOResponse>> getAllProvinces() {

		//List<Province> provinces = provinceService.findAll(); <-- se carga al iniciar la aplicacion
		List<Province> provinces = provinceRepository.getProvinces();
		LOGGER.info("provinces qty: {}", provinces.size());
		return new ResponseEntity<>(ProvinceToDTOMapper.mapProvinces(provinces), HttpStatus.OK);
	}

	@GetMapping("/provinces/{PROVINCE_ID}/municipalities")
	public ResponseEntity<List<Municipality>> getMunicipios(@PathVariable("PROVINCE_ID") int provinceId,
			@RequestParam(value = "qty", required = false) Integer qty) {
		return new ResponseEntity<>(provinceService.findMunicipios(provinceId, qty), HttpStatus.OK);
	}

}
