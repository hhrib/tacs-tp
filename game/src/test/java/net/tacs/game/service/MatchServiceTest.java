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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tacs.game.GameApplication;
import net.tacs.game.services.SecurityProviderService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.Match;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import net.tacs.game.model.bean.CreateMatchBean;
import net.tacs.game.services.MatchService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class MatchServiceTest {

    @Autowired
    private MatchService matchService;

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
    public void createMatchOKTest() throws MatchException {

        CreateMatchBean bean = new CreateMatchBean();
        bean.setMunicipalitiesQty(6);
        bean.setProvinceId(99999997L);
        bean.setUserIds(Arrays.asList("ABC1","ABC2"));

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

    @Test
    public void whenSerializingJava8DateWithCustomSerializer_thenCorrect()
            throws JsonProcessingException {

        LocalDateTime date = LocalDateTime.of(2014, 12, 20, 2, 30);
        Match match = new Match();
        match.setDate(LocalDateTime.now());

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(match);
        assertThat(result, containsString("2014-12-20T02:30:00" ));
    }



}