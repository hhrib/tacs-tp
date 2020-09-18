package net.tacs.game.service;

import net.tacs.game.model.Centroide;
import net.tacs.game.model.MatchConfiguration;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.services.MunicipalityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MunicipalityServiceTest {

    @Autowired
    private MunicipalityService municipalityService;

    @Test
    void attackSuccessfullMunicipality()
    {
        Municipality Lanus = new Municipality("Lanus");
        Municipality Avellaneda = new Municipality("Avellaneda");
        MatchConfiguration Config = new MatchConfiguration();
        Config.setMaxHeight(2000D);
        Config.setMinHeight(1000D);
        Config.setMaxDist(20D);
        Config.setMinDist(10D);

        Centroide lanusCentroide = Mockito.mock(Centroide.class);
        Centroide avellanedaCentroide = new Centroide();

        Lanus.setCentroide(lanusCentroide);
        Avellaneda.setCentroide(avellanedaCentroide);
        Mockito.when(lanusCentroide.getDistance(avellanedaCentroide)).thenReturn(15D);

        Lanus.setElevation(1500D);
        Avellaneda.setElevation(1500D);

        Lanus.setGauchosQty(300);
        Avellaneda.setGauchosQty(100);

        Avellaneda.setState(MunicipalityState.DEFENSE);

        assertEquals(1, municipalityService.attackMunicipality(Lanus, Avellaneda, Config, 250));
    }

    @Test
    void attackRepelledMunicipality()
    {
        Municipality Lanus = new Municipality("Lanus");
        Municipality Avellaneda = new Municipality("Avellaneda");
        MatchConfiguration Config = new MatchConfiguration();
        Config.setMaxHeight(2000D);
        Config.setMinHeight(1000D);
        Config.setMaxDist(20D);
        Config.setMinDist(10D);

        Centroide lanusCentroide = Mockito.mock(Centroide.class);
        Centroide avellanedaCentroide = new Centroide();

        Lanus.setCentroide(lanusCentroide);
        Avellaneda.setCentroide(avellanedaCentroide);
        Mockito.when(lanusCentroide.getDistance(avellanedaCentroide)).thenReturn(15D);

        Lanus.setElevation(1500D);
        Avellaneda.setElevation(1500D);

        Lanus.setGauchosQty(100);
        Avellaneda.setGauchosQty(100);

        Avellaneda.setState(MunicipalityState.PRODUCTION);

        assertEquals(0, municipalityService.attackMunicipality(Lanus, Avellaneda, Config, 100));
    }
}