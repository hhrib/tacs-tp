package net.tacs.game.controller;

import net.tacs.game.model.Municipality;
import net.tacs.game.model.MunicipalityState;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Municipalities {

    private static final Logger LOGGER = LoggerFactory.getLogger(Municipality.class);

    @GetMapping("/municipalities")
    public List<Municipality> getAllMunicipalities() {
        List<Municipality> municipalities = new ArrayList<>();
        Municipality municipalityDefense = new Municipality("Cordoba");
        municipalityDefense.setProvince(new Province("Cordoba"));
        municipalityDefense.setUser(new User("testUser"));
        municipalityDefense.setGauchos(5);
        municipalityDefense.setHeight(100);
        municipalityDefense.setState(MunicipalityState.DEFENSE);
        LOGGER.info(municipalityDefense.toString());
        municipalities.add(municipalityDefense);

        Municipality municipalityProduction = new Municipality("Alta Gracia");
        municipalityProduction.setProvince(new Province("Cordoba"));
        municipalityProduction.setUser(new User("testUserRival"));
        municipalityProduction.setGauchos(5);
        municipalityProduction.setHeight(100);
        municipalityProduction.setState(MunicipalityState.PRODUCTION);
        LOGGER.info(municipalityProduction.toString());
        municipalities.add(municipalityProduction);
        return municipalities;
    }

}
