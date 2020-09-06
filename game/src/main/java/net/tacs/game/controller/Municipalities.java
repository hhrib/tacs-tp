package net.tacs.game.controller;

import net.tacs.game.model.Municipality;
import net.tacs.game.model.enums.MunicipalityState;
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

        municipalityDefense.setOwner(new User("testUser"));
        municipalityDefense.setGauchosQty(5);
        municipalityDefense.setElevation(100D);
        municipalityDefense.setState(MunicipalityState.DEFENSE);
        LOGGER.info(municipalityDefense.toString());
        municipalities.add(municipalityDefense);

        Municipality municipalityProduction = new Municipality("Alta Gracia");
        municipalityProduction.setProvince(new Province("Cordoba"));
        municipalityProduction.setOwner(new User("testUserRival"));
        municipalityProduction.setGauchosQty(5);
        municipalityProduction.setElevation(100D);
        municipalityProduction.setState(MunicipalityState.PRODUCTION);
        LOGGER.info(municipalityProduction.toString());
        municipalities.add(municipalityProduction);
        return municipalities;
    }

}
