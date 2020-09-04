package net.tacs.game.controller;

import net.tacs.game.model.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Provinces {

    private static final Logger LOGGER = LoggerFactory.getLogger(Province.class);

    @GetMapping("/provinces")
    public List<Province> getAllProvinces() {
        List<Province> provinces = new ArrayList<>();
        Province province = new Province("Buenos Aires");
        provinces.add(province);
        LOGGER.info(province.toString());
        return provinces;
    }

}
