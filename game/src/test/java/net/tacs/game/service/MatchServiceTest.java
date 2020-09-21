package net.tacs.game.service;

import static net.tacs.game.GameApplication.addMunicipality;
import static net.tacs.game.GameApplication.addProvince;
import static net.tacs.game.GameApplication.addUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.tacs.game.model.*;
import net.tacs.game.services.MunicipalityService;
import net.tacs.game.services.impl.MatchServiceImpl;
import net.tacs.game.services.impl.MunicipalityServiceImpl;
import net.tacs.game.GameApplication;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.bean.CreateMatchBean;
import net.tacs.game.services.MatchService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class MatchServiceTest {

    @InjectMocks
    private MatchService matchService = new MatchServiceImpl();
    @Mock
    private MunicipalityService municipalityService;

    private final User user1 = new User("Pepe");
    private final User user2 = new User("Paula");
    private final Province buenosAires = new Province("Buenos Aires");
    private final Municipality lanus = new Municipality("Lanus");
    private final Municipality avellaneda = new Municipality("Avellaneda");
    private final Municipality quilmes = new Municipality("Quilmes");
    private final Municipality tigre = new Municipality("Tigre");
    private final Municipality lomas = new Municipality("Lomas de Zamora");
    private final Municipality matanza = new Municipality("La Matanza");

    @Test
    void createMatchOKTest() throws MatchException, InterruptedException {

        CreateMatchBean bean = new CreateMatchBean();
        bean.setMunicipalitiesQty(6);
        bean.setProvinceId(99999997L);
        bean.setUserIds(Arrays.asList("ABC1","ABC2"));
        bean.setConfigs(Arrays.asList(1.25D, 15D, 10D, 2D, 2D, 3000D));

        addProvince(buenosAires);
        addMunicipality(lanus);
        addMunicipality(avellaneda);
        addMunicipality(quilmes);
        addMunicipality(tigre);
        addMunicipality(lomas);
        addMunicipality(matanza);
        GameApplication.setUsers(Arrays.asList(user1,user2));

        List<Municipality> municipalityList = new ArrayList<>();
        municipalityList.add(lanus);
        municipalityList.add(quilmes);
        municipalityList.add(avellaneda);
        municipalityList.add(tigre);
        municipalityList.add(lomas);
        municipalityList.add(matanza);

        buenosAires.setId(99999997L);
        buenosAires.setMunicipalities(municipalityList);

        user1.setId("ABC1");
        user2.setId("ABC2");

        lanus.setGauchosQty(300);
        avellaneda.setGauchosQty(300);
        quilmes.setGauchosQty(300);
        tigre.setGauchosQty(300);
        lomas.setGauchosQty(300);
        matanza.setGauchosQty(300);

        lanus.setCentroide(new Centroide("0", "0"));
        avellaneda.setCentroide(new Centroide("1", "1"));
        quilmes.setCentroide(new Centroide("2", "2"));
        tigre.setCentroide(new Centroide("3", "3"));
        lomas.setCentroide(new Centroide("4", "4"));
        matanza.setCentroide(new Centroide("5", "5"));

        Mockito.when(municipalityService.getElevation(lanus.getCentroide())).thenReturn(3000D);
        Mockito.when(municipalityService.getElevation(avellaneda.getCentroide())).thenReturn(3000D);
        Mockito.when(municipalityService.getElevation(quilmes.getCentroide())).thenReturn(3000D);
        Mockito.when(municipalityService.getElevation(tigre.getCentroide())).thenReturn(3000D);
        Mockito.when(municipalityService.getElevation(lomas.getCentroide())).thenReturn(3000D);
        Mockito.when(municipalityService.getElevation(matanza.getCentroide())).thenReturn(3000D);

        Match match = matchService.createMatch(bean);

        //chequea que se hayan agregado correctamente los usuarios, la provincia y los municipios
        assertEquals("Pepe" , match.getUsers().get(0).getUsername());
        assertEquals("Paula" , match.getUsers().get(1).getUsername());
        assertEquals("Buenos Aires", match.getMap().getNombre());
        assertTrue(match.getMap().getMunicipalities().contains(lanus));
        assertTrue(match.getMap().getMunicipalities().contains(avellaneda));
        assertTrue(match.getMap().getMunicipalities().contains(quilmes));
        assertTrue(match.getMap().getMunicipalities().contains(tigre));
        assertTrue(match.getMap().getMunicipalities().contains(lomas));
        assertTrue(match.getMap().getMunicipalities().contains(matanza));

        //chequea que cada usuario tenga la misma cantidad de municipios
        int user1Munis = 0;
        int user2Munis = 0;

        for(Municipality aMuni : match.getMap().getMunicipalities())
        {
            switch (aMuni.getOwner().getId())
            {
                case "ABC1":
                {
                    user1Munis++;
                    break;
                }
                case "ABC2":
                {
                    user2Munis++;
                    break;
                }
            }

        }

        assertEquals(3, user1Munis);
        assertEquals(3, user2Munis);
    }

    @Test
    void CalculateMatchConfig(){
        Match match = new Match();
        MatchConfiguration matchConfiguration = new MatchConfiguration();
        match.setMap(buenosAires);
        match.getMap().setMunicipalities(Arrays.asList(lanus, avellaneda, quilmes, tigre, lomas, matanza));
        match.setConfig(matchConfiguration);

        lanus.setElevation(3000D);
        avellaneda.setElevation(2900D);
        quilmes.setElevation(2800D);
        tigre.setElevation(2700D);
        lomas.setElevation(2600D);
        matanza.setElevation(2500D);

        //longest distance
        lanus.setCentroide(new Centroide("100", "100"));
        avellaneda.setCentroide(new Centroide("1", "1"));

        //shortest distance
        quilmes.setCentroide(new Centroide("30", "29"));
        tigre.setCentroide(new Centroide("29", "30"));

        lomas.setCentroide(new Centroide("50", "80"));
        matanza.setCentroide(new Centroide("80", "50"));

        matchService.CalculateConfigVariables(match);

        assertEquals(3000, match.getConfig().getMaxHeight());
        assertEquals(2500, match.getConfig().getMinHeight());
        assertEquals(140.0071426749364D, match.getConfig().getMaxDist());
        assertEquals(1.4142135623730951D, match.getConfig().getMinDist());
    }
}