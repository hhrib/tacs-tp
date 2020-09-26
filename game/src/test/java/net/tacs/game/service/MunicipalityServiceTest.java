package net.tacs.game.service;

import net.tacs.game.GameApplication;
import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.*;
import net.tacs.game.model.dto.MoveGauchosDTO;
import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.services.MunicipalityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class MunicipalityServiceTest {

    @Autowired
    private MunicipalityService municipalityService;

    private User user1;
    private User user2;
    private Province buenosAires;
    private Municipality Lanus;
    private Municipality Avellaneda;
    private Municipality Quilmes;
    private Municipality Tigre;
    private Municipality Lomas;
    private Municipality Matanza;
    private MatchConfiguration configuration;

    @Before
    public void setUp() {
        GameApplication.setToken("");
        GameApplication.setUsers(new ArrayList<>());
        GameApplication.setMunicipalities(new ArrayList<>());
        GameApplication.setMatches(new ArrayList<>());
        GameApplication.setProvinces(new ArrayList<>());
        user1 = new User("Pepe");
        user2 = new User("Paula");
        buenosAires = new Province("Buenos Aires");
        Lanus = new Municipality("Lanus");
        Avellaneda = new Municipality("Avellaneda");
        Quilmes = new Municipality("Quilmes");
        Tigre = new Municipality("Tigre");
        Lomas = new Municipality("Lomas de Zamora");
        Matanza = new Municipality("La Matanza");
        configuration = new MatchConfiguration();
    }

    @Test
    public void attackSuccessfullMunicipality() {
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
    public void attackRepelledMunicipality() {
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

    @Test
    public void gauchosProducedOnProductionMunicipality() {
        configuration.setMaxHeight(2000D);
        configuration.setMinHeight(1000D);
        configuration.setMaxDist(20D);
        configuration.setMinDist(10D);

        Lanus.setElevation(1500D);

        Lanus.setGauchosQty(0);
        Lanus.setState(MunicipalityState.PRODUCTION);
        Lanus.produceGauchos(configuration);

        assertEquals(11, Lanus.getGauchosQty());
    }

    @Test
    public void gauchosProducedOnDefenseMunicipality() {
        configuration.setMaxHeight(2000D);
        configuration.setMinHeight(1000D);
        configuration.setMaxDist(20D);
        configuration.setMinDist(10D);

        Lanus.setElevation(1500D);

        Lanus.setGauchosQty(0);
        Lanus.setState(MunicipalityState.DEFENSE);
        Lanus.produceGauchos(configuration);

        assertEquals(7, Lanus.getGauchosQty());
    }

    @Test
    public void municipalityMoveGauchosOK() throws MatchException {
        Match match = new Match();
        match.setId(1235L);
        match.setMap(buenosAires);
        match.setConfig(configuration);
        buenosAires.setMunicipalities(Arrays.asList(Lanus, Avellaneda, Quilmes));

        Lanus.setId(77777);
        Avellaneda.setId(99999);
        Quilmes.setId(88888);

        Lanus.setGauchosQty(3000);
        Avellaneda.setGauchosQty(2500);
        Quilmes.setGauchosQty(1500);

        GameApplication.addMatch(match);

        MoveGauchosDTO dto = new MoveGauchosDTO();
        dto.setMatchId(match.getId());
        dto.setIdOriginMuni(99999);
        dto.setIdDestinyMuni(88888);
        dto.setQty(500);

        municipalityService.moveGauchos(dto);

        assertEquals(3000, Lanus.getGauchosQty());
        assertEquals(2000, Avellaneda.getGauchosQty());
        assertEquals(2000, Quilmes.getGauchosQty());
    }
}