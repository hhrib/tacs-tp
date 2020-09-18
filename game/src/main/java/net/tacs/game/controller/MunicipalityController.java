package net.tacs.game.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import net.tacs.game.model.Centroide;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.services.MunicipalityService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class MunicipalityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Municipality.class);
    
    @Autowired
	private MunicipalityService municipalityService;

    //TODO quizas solo se necesite de la parida en juego y no todas
    @GetMapping("/municipalities")
    public ResponseEntity<List<Municipality>> getAllMunicipalities() {

        //TODO de prueba
        List<Municipality> municipalities = new ArrayList<>();
        Municipality municipalityDefense = new Municipality("Cordoba");

        municipalityDefense.setOwner(new User("testUser"));
        municipalityDefense.setGauchosQty(5);
        municipalityDefense.setElevation(100D);
        municipalityDefense.setState(MunicipalityState.DEFENSE);
        LOGGER.info(municipalityDefense.toString());
        municipalities.add(municipalityDefense);

        Municipality municipalityProduction = new Municipality("Alta Gracia");
        municipalityProduction.setOwner(new User("testUserRival"));
        municipalityProduction.setGauchosQty(5);
        municipalityProduction.setElevation(100D);
        municipalityProduction.setState(MunicipalityState.PRODUCTION);
        LOGGER.info(municipalityProduction.toString());
        municipalities.add(municipalityProduction);

        return new ResponseEntity<>(municipalities, HttpStatus.OK);
    }

    @GetMapping("/elevation/{LAT}/{LON}")
	public Double getElevation(@PathVariable("LAT") String lat, @PathVariable("LON") String lon) {
		return municipalityService.getElevation(new Centroide(lat, lon));
	}
}
