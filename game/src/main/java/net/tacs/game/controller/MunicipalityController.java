package net.tacs.game.controller;

import java.util.ArrayList;
import java.util.List;

import net.tacs.game.model.interfaces.MunicipalityDefense;
import net.tacs.game.model.interfaces.MunicipalityProduction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.tacs.game.model.Centroide;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.User;
//import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.services.MunicipalityService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://tacswololo.tk"})
public class MunicipalityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MunicipalityController.class);
    
    @Autowired
	private MunicipalityService municipalityService;

    @GetMapping("/municipalities")
    public ResponseEntity<List<Municipality>> getAllMunicipalities() {

        List<Municipality> municipalities = new ArrayList<>();
        Municipality municipalityDefense = new Municipality("Cordoba");
        municipalityDefense.setOwner(new User("testUser"));
        municipalityDefense.setGauchosQty(5);
        municipalityDefense.setElevation(100D);
        municipalityDefense.setState(new MunicipalityDefense());
        LOGGER.info(municipalityDefense.toString());
        municipalities.add(municipalityDefense);

        Municipality municipalityProduction = new Municipality("Alta Gracia");
        municipalityProduction.setOwner(new User("testUserRival"));
        municipalityProduction.setGauchosQty(5);
        municipalityProduction.setElevation(100D);
        municipalityProduction.setState(new MunicipalityProduction());
        LOGGER.info(municipalityProduction.toString());
        municipalities.add(municipalityProduction);

        return new ResponseEntity<>(municipalities, HttpStatus.OK);
    }

    @GetMapping("/municipalities/elevation/{LAT}/{LON}")
	public Double getElevation(@PathVariable("LAT") String lat, @PathVariable("LON") String lon) {
		return municipalityService.getElevation(new Centroide(lat, lon));
	}
}
