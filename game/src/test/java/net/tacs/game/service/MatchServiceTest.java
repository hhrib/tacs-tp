package net.tacs.game.service;

import static net.tacs.game.GameApplication.addMunicipality;
import static net.tacs.game.GameApplication.addProvince;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.tacs.game.model.*;
import net.tacs.game.services.MunicipalityService;
import net.tacs.game.services.impl.MatchServiceImpl;
import net.tacs.game.services.impl.MunicipalityServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tacs.game.GameApplication;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import net.tacs.game.services.SecurityProviderService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.bean.CreateMatchBean;
import net.tacs.game.services.MatchService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class MatchServiceTest {

    @InjectMocks
    private MatchService matchService = new MatchServiceImpl();
    @Mock
    private MunicipalityService municipalityService;

    @Mock
    private SecurityProviderService securityProviderService;

    private User user1;
    private User user2;
    private Province buenosAires;
    private Municipality lanus;
    private Municipality avellaneda;
    private Municipality quilmes;
    private Municipality tigre;
    private Municipality lomas;
    private Municipality matanza;

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
        lanus = new Municipality("Lanus");
        avellaneda = new Municipality("Avellaneda");
        quilmes = new Municipality("Quilmes");
        tigre = new Municipality("Tigre");
        lomas = new Municipality("Lomas de Zamora");
        matanza = new Municipality("La Matanza");
    }

    @Test
    public void createMatchOKTest() throws MatchException, InterruptedException {

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

        lanus.setElevation(3D);
        avellaneda.setElevation(3D);
        quilmes.setElevation(3D);
        tigre.setElevation(3D);
        lomas.setElevation(3D);
        matanza.setElevation(3D);

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

    @Test(expected = MatchException.class)
    public void createMatchUsersNotFoundTest() throws MatchException {
        CreateMatchBean bean = new CreateMatchBean();
        bean.setMunicipalitiesQty(6);
        bean.setProvinceId(99999997L);
        bean.setUserIds(Arrays.asList("ABC1","ABC2"));

        addProvince(buenosAires);
        addMunicipality(lanus);
        List<Municipality> municipalityList = new ArrayList<>();
        municipalityList.add(lanus);
        buenosAires.setId(99999997L);
        buenosAires.setMunicipalities(municipalityList);
        user1.setId("user1");
        user2.setId("user2");
        GameApplication.setUsers(Arrays.asList(user1,user2));

        Match match = matchService.createMatch(bean);

    }

    @Test(expected = MatchException.class)
    public void createMatchWithNoUsersFailTest() throws MatchException {
        CreateMatchBean bean = new CreateMatchBean();
        bean.setUserIds(new ArrayList<>());

        matchService.createMatch(bean);

    }

    @Test(expected = MatchException.class)
    public void createMatchWithOneUserFailTest() throws MatchException {
        CreateMatchBean bean = new CreateMatchBean();
        bean.setUserIds(Arrays.asList("userTestId"));

        matchService.createMatch(bean);

    }

    @Test(expected = MatchException.class)
    public void createMatchWithNoMapFailTest() throws MatchException {
        CreateMatchBean bean = new CreateMatchBean();
        bean.setUserIds(Arrays.asList("userTestId1", "userTestId2"));

        matchService.createMatch(bean);

    }

    @Test(expected = MatchException.class)
    public void createMatchWithNotEnoughMunicipalitiesFailTest() throws MatchException {
        CreateMatchBean bean = new CreateMatchBean();
        bean.setMunicipalitiesQty(6);
        bean.setProvinceId(99999997L);
        bean.setUserIds(Arrays.asList("ABC1", "ABC2"));

        addProvince(buenosAires);
        addMunicipality(lanus);
        List<Municipality> municipalityList = new ArrayList<>();
        municipalityList.add(lanus);
        addMunicipality(avellaneda);
        addMunicipality(quilmes);
        buenosAires.setId(99999997L);
        buenosAires.setMunicipalities(municipalityList);
        user1.setId("ABC1");
        user2.setId("ABC2");
        lanus.setElevation(3D);
        lanus.setGauchosQty(300);
        GameApplication.setUsers(Arrays.asList(user1, user2));

        matchService.createMatch(bean);

    }

    @Test
    public void findMatchByDatesOK() throws MatchException {
        Match match = new Match();
        match.setId(1234L);
        match.setDate(LocalDateTime.of(2020, 9, 10, 0, 0));
        GameApplication.setMatches(Arrays.asList(match));
        LocalDate dateFrom = LocalDate.of(2020, 9, 10);
        LocalDate dateTo = LocalDate.of(2020, 9, 20);

        List<Match> matches = matchService.findMatchesByDate(dateFrom.format(DateTimeFormatter.ISO_LOCAL_DATE), dateTo.format(DateTimeFormatter.ISO_LOCAL_DATE));

        assertTrue(!matches.isEmpty() && matches.get(0).getId().equals(match.getId()));

    }

    @Test(expected = MatchException.class)
    public void findMatchByDatesNullByDateFromNullTest() throws MatchException {
        List<Match> matches = matchService.findMatchesByDate(null, "dummyTest");
    }

    @Test(expected = MatchException.class)
    public void findMatchByDatesNullByDateToNullTest() throws MatchException {
        List<Match> matches = matchService.findMatchesByDate("dummyTest", null);
    }

    @Test(expected = MatchException.class)
    public void findMatchByDatesDateFromAfterDateToFailTest() throws MatchException {
        Match match = new Match();
        match.setId(1234L);
        match.setDate(LocalDateTime.of(2020, 9, 10, 0, 0));
        GameApplication.setMatches(Arrays.asList(match));
        LocalDate dateTo = LocalDate.of(2020, 9, 10);
        LocalDate dateFrom = LocalDate.of(2020, 9, 20);

        matchService.findMatchesByDate(dateFrom.format(DateTimeFormatter.ISO_LOCAL_DATE), dateTo.format(DateTimeFormatter.ISO_LOCAL_DATE));

    }

    @Test(expected = MatchException.class)
    public void findMatchByDatesDateFormatFailTest() throws MatchException {
        List<Match> matches = matchService.findMatchesByDate("dummyTest", "dummyTest");
    }

    @Test
    public void getMatchByIdOKTest() throws MatchException {
        Match match = new Match();
        match.setId(1234L);
        GameApplication.setMatches(Arrays.asList(match));
        Match matchRetrieved = matchService.getMatchById("1234");

        assertTrue(matchRetrieved.equals(match));
    }

    @Test(expected = MatchException.class)
    public void getMatchByIdFailByIdNullTest() throws MatchException {
        Match matchRetrieved = matchService.getMatchById(null);
    }

    @Test(expected = MatchException.class)
    public void getMatchByIdFailByNumberFormatTest() throws MatchException {
        Match matchRetrieved = matchService.getMatchById("wrongNumberFormat");
    }

}