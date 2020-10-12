package net.tacs.game.service;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import net.tacs.game.exceptions.MatchNotPlayerTurnException;
import net.tacs.game.exceptions.MatchNotStartedException;
import net.tacs.game.model.*;
import net.tacs.game.model.dto.MatchesStatisticsDTO;
import net.tacs.game.model.dto.RetireDTO;
import net.tacs.game.model.enums.MatchState;
import net.tacs.game.model.interfaces.MunicipalityDefense;
import net.tacs.game.model.interfaces.MunicipalityProduction;
import net.tacs.game.repositories.MatchRepository;
import net.tacs.game.repositories.ProvinceRepository;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.services.MunicipalityService;
import net.tacs.game.GameApplication;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import net.tacs.game.services.SecurityProviderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.dto.CreateMatchDTO;
import net.tacs.game.services.MatchService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class MatchServiceTest {

    @Autowired
    private MatchService matchService;

    @MockBean
    private MunicipalityService municipalityService;

    @MockBean
    private SecurityProviderService securityProviderService;

    @MockBean
    private MatchRepository matchRepository;

    @MockBean
    private ProvinceRepository provinceRepository;

    @MockBean
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private Province buenosAires;
    private Municipality lanus;
    private Municipality avellaneda;
    private Municipality quilmes;
    private Municipality tigre;
    private Municipality lomas;
    private Municipality matanza;
    private MatchConfiguration configuration;
    private MunicipalityDefense defenseState;
    private MunicipalityProduction prodState;

    @Before
    public void setUp() {
        GameApplication.setToken("");
        user1 = new User("Pepe");
        user2 = new User("Paula");
        buenosAires = new Province("Buenos Aires");
        lanus = new Municipality("Lanus");
        lanus.setId(11111);
        avellaneda = new Municipality("Avellaneda");
        avellaneda.setId(22222);
        quilmes = new Municipality("Quilmes");
        quilmes.setId(33333);
        tigre = new Municipality("Tigre");
        tigre.setId(44444);
        lomas = new Municipality("Lomas de Zamora");
        lomas.setId(55555);
        matanza = new Municipality("La Matanza");
        matanza.setId(66666);
        configuration = new MatchConfiguration();
        defenseState = new MunicipalityDefense();
        prodState = new MunicipalityProduction();
        defenseState.createState(1.25D, 10D, prodState);
        prodState.createState(1D, 15D, defenseState);
    }

    @Test
    public void createMatchOKTest() throws MatchException, InterruptedException {

        CreateMatchDTO dto = new CreateMatchDTO();
        dto.setMunicipalitiesQty(6);
        dto.setProvinceId(99999997L);
        dto.setUserIds(Arrays.asList("ABC1","ABC2"));
        dto.setConfigs(Arrays.asList(1.25D, 15D, 10D, 2D, 2D, 3000D));

        provinceRepository.add(buenosAires);
        userRepository.setUsers(Arrays.asList(user1,user2));

        buenosAires.setId(99999997L);
        buenosAires.setMunicipalities(Arrays.asList(lanus, avellaneda, quilmes, tigre, lomas, matanza));

        user1.setId("ABC1");
        user2.setId("ABC2");

        lanus.setCentroide(new Centroide("0", "0"));
        avellaneda.setCentroide(new Centroide("1", "1"));
        quilmes.setCentroide(new Centroide("2", "2"));
        tigre.setCentroide(new Centroide("3", "3"));
        lomas.setCentroide(new Centroide("4", "4"));
        matanza.setCentroide(new Centroide("5", "5"));

        Map<Integer, Double> elevations = new HashMap<>();
        elevations.put(1, 1D);
        elevations.put(2, 2D);
        elevations.put(3, 3D);
        elevations.put(4, 4D);
        elevations.put(5, 5D);
        elevations.put(6, 6D);

        Mockito.when(userRepository.findById("ABC1")).thenReturn(java.util.Optional.ofNullable(user1));
        Mockito.when(userRepository.findById("ABC2")).thenReturn(java.util.Optional.ofNullable(user2));
        Mockito.when(provinceRepository.findById(99999997L)).thenReturn(java.util.Optional.ofNullable(buenosAires));
        Mockito.when(municipalityService.getElevations(ArgumentMatchers.anyList())).thenReturn(elevations);

        Match match = matchService.createMatch(dto);

        //chequea que se hayan agregado correctamente los usuarios, la provincia y los municipios
        assertEquals("Pepe" , match.getUsers().get(0).getUsername());
        assertEquals("Paula" , match.getUsers().get(1).getUsername());
        assertEquals("Buenos Aires", match.getMap().getNombre());
        assertTrue(match.getMap().getMunicipalities().containsValue(lanus));
        assertTrue(match.getMap().getMunicipalities().containsValue(avellaneda));
        assertTrue(match.getMap().getMunicipalities().containsValue(quilmes));
        assertTrue(match.getMap().getMunicipalities().containsValue(tigre));
        assertTrue(match.getMap().getMunicipalities().containsValue(lomas));
        assertTrue(match.getMap().getMunicipalities().containsValue(matanza));

        //chequea que cada usuario tenga la misma cantidad de municipios
        assertEquals(3, user1.municipalitiesOwning(new ArrayList<>(match.getMap().getMunicipalities().values())));
        assertEquals(3, user2.municipalitiesOwning(new ArrayList<>(match.getMap().getMunicipalities().values())));
    }

    @Test
    public void CalculateMatchConfig(){
        Match match = new Match();
        match.setMap(buenosAires);
        match.getMap().setMunicipalities(Arrays.asList(lanus, avellaneda, quilmes, tigre, lomas, matanza));
        match.setConfig(configuration);

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

        matchService.calculateConfigVariables(match);

        assertEquals(3000, match.getConfig().getMaxHeight(), 0.1D);
        assertEquals(2500, match.getConfig().getMinHeight(), 0.1D);
        assertEquals(140.00714D, match.getConfig().getMaxDist(), 0.0001D);
        assertEquals(1.41421D, match.getConfig().getMinDist(), 0.0001D);
    }

    @Test
    public void assignPlayersTurns()
    {
        Match match = new Match();
        match.setConfig(configuration);

        User user3 = new User("Papa");
        User user4 = new User("Pipi");

        match.setUsers(Arrays.asList(user1, user2, user3, user4));

        matchService.assignPlayersOrder(match);

        assertEquals(4, match.getConfig().getPlayersTurns().size());
        assertNotNull(match.getTurnPlayer());
    }

    @Test(expected = MatchException.class)
    public void createMatchUsersNotFoundTest() throws MatchException, InterruptedException {
        CreateMatchDTO dto = new CreateMatchDTO();
        dto.setMunicipalitiesQty(6);
        dto.setProvinceId(99999997L);
        dto.setUserIds(Arrays.asList("ABC1","ABC2"));

        provinceRepository.add(buenosAires);
        List<Municipality> municipalityList = new ArrayList<>();
        municipalityList.add(lanus);
        buenosAires.setId(99999997L);
        buenosAires.setMunicipalities(municipalityList);
        user1.setId("user1");
        user2.setId("user2");
        userRepository.setUsers(Arrays.asList(user1,user2));

        Mockito.when(provinceRepository.findById(99999997L)).thenReturn(java.util.Optional.ofNullable(buenosAires));
        Mockito.when(userRepository.findById("ABC1")).thenReturn(java.util.Optional.empty());
        Mockito.when(userRepository.findById("ABC2")).thenReturn(java.util.Optional.empty());

        Match match = matchService.createMatch(dto);
    }

    @Test(expected = MatchException.class)
    public void createMatchWithNoUsersFailTest() throws MatchException, InterruptedException {
        CreateMatchDTO dto = new CreateMatchDTO();
        dto.setUserIds(new ArrayList<>());

        matchService.createMatch(dto);
    }

    @Test(expected = MatchException.class)
    public void createMatchWithOneUserFailTest() throws MatchException, InterruptedException {
        CreateMatchDTO dto = new CreateMatchDTO();
        dto.setUserIds(Arrays.asList("userTestId"));

        matchService.createMatch(dto);
    }

    @Test(expected = MatchException.class)
    public void createMatchWithNoMapFailTest() throws MatchException, InterruptedException {
        CreateMatchDTO dto = new CreateMatchDTO();
        dto.setUserIds(Arrays.asList("userTestId1", "userTestId2"));

        matchService.createMatch(dto);
    }

    @Test(expected = MatchException.class)
    public void createMatchWithNotEnoughMunicipalitiesFailTest() throws MatchException, InterruptedException {
        CreateMatchDTO dto = new CreateMatchDTO();
        dto.setMunicipalitiesQty(6);
        dto.setProvinceId(99999997L);
        dto.setUserIds(Arrays.asList("ABC1", "ABC2"));
        dto.setConfigs(Arrays.asList(1.25D, 15D, 10D, 2D, 2D, 3000D));

        provinceRepository.add(buenosAires);
        List<Municipality> municipalityList = new ArrayList<>();
        municipalityList.add(lanus);
        buenosAires.setId(99999997L);
        buenosAires.setMunicipalities(municipalityList);
        user1.setId("ABC1");
        user2.setId("ABC2");
        lanus.setElevation(3D);
        lanus.setGauchosQty(300);
        userRepository.setUsers(Arrays.asList(user1, user2));

        Mockito.when(provinceRepository.findById(99999997L)).thenReturn(java.util.Optional.ofNullable(buenosAires));

        matchService.createMatch(dto);
    }

    @Test
    public void findMatchByDatesOK() throws MatchException {
        Match match = new Match();
        match.setId(1234L);
        match.setDate(LocalDateTime.of(2020, 9, 10, 0, 0));
        Mockito.when(matchRepository.getMatches()).thenReturn(Arrays.asList(match));
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
        Mockito.when(matchRepository.getMatches()).thenReturn(Arrays.asList(match));
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
        Mockito.when(matchRepository.findById(1234L)).thenReturn(java.util.Optional.of(match));
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
    public void passTurnPlayerLastOnTheList() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        MatchConfiguration config = new MatchConfiguration();
        match.setId(123456L);
        match.setState(MatchState.IN_PROGRESS);
        match.setConfig(config);
        match.setUsers(Arrays.asList(user1, user2));
        match.setTurnPlayer(user2);

        user1.setId("ABC1");
        user2.setId("ABC2");

        config.setPlayersTurns(Arrays.asList(user1, user2));

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));

        matchService.passTurn("123456", "ABC2");

        assertEquals(user1, match.getTurnPlayer());
    }

    @Test
    public void passTurnPlayerMiddleOnTheList() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        MatchConfiguration config = new MatchConfiguration();
        match.setId(123456L);
        match.setState(MatchState.IN_PROGRESS);
        match.setConfig(config);
        match.setTurnPlayer(user2);

        User user3 = new User("Laura");

        user1.setId("ABC1");
        user2.setId("ABC2");
        user3.setId("ABC3");

        match.setUsers(Arrays.asList(user1, user2, user3));
        config.setPlayersTurns(Arrays.asList(user1, user2, user3));

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));

        matchService.passTurn("123456", "ABC2");

        assertEquals(user3, match.getTurnPlayer());
    }

    @Test(expected = MatchException.class)
    public void passTurnPlayerNotInMatch() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        MatchConfiguration config = new MatchConfiguration();
        match.setId(123456L);
        match.setState(MatchState.IN_PROGRESS);
        match.setConfig(config);
        match.setUsers(Arrays.asList(user1, user2));
        match.setTurnPlayer(user2);

        User user3 = new User("Laura");

        user1.setId("ABC1");
        user2.setId("ABC2");
        user3.setId("ABC3");

        config.setPlayersTurns(Arrays.asList(user1, user2));

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));

        matchService.passTurn("123456", "ABC3");
    }

    @Test(expected = MatchNotPlayerTurnException.class)
    public void passTurnPlayerNotInTurn() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        MatchConfiguration config = new MatchConfiguration();
        match.setId(123456L);
        match.setState(MatchState.IN_PROGRESS);
        match.setConfig(config);
        match.setUsers(Arrays.asList(user1, user2));
        match.setTurnPlayer(user2);

        user1.setId("ABC1");
        user2.setId("ABC2");

        config.setPlayersTurns(Arrays.asList(user1, user2));

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));

        matchService.passTurn("123456", "ABC1");
    }

    @Test (expected = MatchNotStartedException.class)
    public void passTurnMatchNotStartedThrowsException() throws MatchNotPlayerTurnException, MatchException, MatchNotStartedException {
        Match match = new Match();
        match.setId(123456L);
        match.setState(MatchState.CREATED);

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));

        matchService.passTurn("123456", "ABC1");
    }

    @Test
    public void updateMuniStateOK() throws MatchNotPlayerTurnException, MatchException, MatchNotStartedException {
        Match match = new Match();
        MatchConfiguration config = new MatchConfiguration();
        match.setId(123456L);
        match.setState(MatchState.IN_PROGRESS);
        match.setConfig(config);
        match.setUsers(Arrays.asList(user1, user2));
        match.setTurnPlayer(user2);
        match.setMap(buenosAires);

        lanus.setId(98765);
        lanus.setOwner(user2);
        lanus.setState(defenseState);
        tigre.setId(56789);

        buenosAires.setMunicipalities(Arrays.asList(lanus, tigre));

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));

        //UpdateMunicipalityStateDTO dto = new UpdateMunicipalityStateDTO();
        //dto.setNewState(prodState);

        matchService.updateMunicipalityState("123456", "98765"/*, dto*/);

        assertEquals(prodState, lanus.getState());
    }

    @Test (expected = MatchNotPlayerTurnException.class)
    public void updateMuniStateNotPlayerTurn() throws MatchNotPlayerTurnException, MatchException, MatchNotStartedException {
        Match match = new Match();
        MatchConfiguration config = new MatchConfiguration();
        match.setId(123456L);
        match.setState(MatchState.IN_PROGRESS);
        match.setConfig(config);
        match.setUsers(Arrays.asList(user1, user2));
        match.setTurnPlayer(user1);
        match.setMap(buenosAires);

        lanus.setId(98765);
        lanus.setOwner(user2);
        lanus.setState(defenseState);
        tigre.setId(56789);

        buenosAires.setMunicipalities(Arrays.asList(lanus, tigre));

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));

        //UpdateMunicipalityStateDTO dto = new UpdateMunicipalityStateDTO();
        //dto.setNewState(prodState);

        matchService.updateMunicipalityState("123456", "98765"/*, dto*/);
    }

    @Test (expected = MatchNotStartedException.class)
    public void updateMuniStateMatchNotStartedThrowsException() throws MatchNotPlayerTurnException, MatchException, MatchNotStartedException {
        Match match = new Match();
        match.setId(123456L);
        match.setState(MatchState.CREATED);

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));

        //UpdateMunicipalityStateDTO dto = new UpdateMunicipalityStateDTO();
        //dto.setNewState(prodState);

        matchService.updateMunicipalityState("123456", "98765"/*, dto*/);
    }

    @Test
    public void retireFinishesMatch() throws MatchException {
        Match match = new Match();
        match.setState(MatchState.IN_PROGRESS);
        MatchConfiguration config = new MatchConfiguration();
        match.setId(123456L);
        match.setConfig(config);
        match.setUsers(Arrays.asList(user1, user2));
        config.setPlayersTurns(new LinkedList<User>(Arrays.asList(user1, user2)));

        user1.setId("ABC123");
        user2.setId("CBA321");

        RetireDTO retireDTO = new RetireDTO();
        retireDTO.setPlayerId("ABC123");

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));
        Mockito.when(userRepository.findById("ABC123")).thenReturn(java.util.Optional.of(user1));

        matchService.retireFromMatch("123456", retireDTO);

        assertEquals(MatchState.FINISHED, match.getState());
    }

    @Test
    public void retireCancelsMatch() throws MatchException {
        Match match = new Match();
        match.setState(MatchState.CREATED);
        MatchConfiguration config = new MatchConfiguration();
        match.setId(123456L);
        match.setConfig(config);
        match.setUsers(Arrays.asList(user1, user2));
        config.setPlayersTurns(new LinkedList<User>(Arrays.asList(user1, user2)));

        user1.setId("ABC123");
        user2.setId("CBA321");

        RetireDTO retireDTO = new RetireDTO();
        retireDTO.setPlayerId("ABC123");

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));
        Mockito.when(userRepository.findById("ABC123")).thenReturn(java.util.Optional.of(user1));

        matchService.retireFromMatch("123456", retireDTO);

        assertEquals(MatchState.CANCELLED, match.getState());
    }

    @Test
    public void retireMatchOk() throws MatchException {
        Match match = new Match();
        match.setState(MatchState.IN_PROGRESS);
        MatchConfiguration config = new MatchConfiguration();
        match.setId(123456L);
        match.setConfig(config);
        match.setMap(buenosAires);
        buenosAires.setMunicipalities(Arrays.asList(lanus, avellaneda, quilmes, tigre, matanza, lomas));

        User user3 = new User("Paola");

        match.setUsers(Arrays.asList(user1, user2, user3));
        config.setPlayersTurns(new LinkedList<User>(Arrays.asList(user1, user2, user3)));

        user1.setId("ABC123");
        user2.setId("CBA321");
        user3.setId("BBB222");

        lanus.setOwner(user1);
        avellaneda.setOwner(user1);
        quilmes.setOwner(user2);
        tigre.setOwner(user2);
        matanza.setOwner(user3);
        lomas.setOwner(user3);

        RetireDTO retireDTO = new RetireDTO();
        retireDTO.setPlayerId("BBB222");

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));
        Mockito.when(userRepository.findById("BBB222")).thenReturn(java.util.Optional.of(user3));

        matchService.retireFromMatch("123456", retireDTO);

        assertEquals(3, user1.municipalitiesOwning(new ArrayList<>(match.getMap().getMunicipalities().values())));
        assertEquals(3, user2.municipalitiesOwning(new ArrayList<>(match.getMap().getMunicipalities().values())));
        assertEquals(0, user3.municipalitiesOwning(new ArrayList<>(match.getMap().getMunicipalities().values())));
        assertFalse(match.getConfig().getPlayersTurns().contains(user3));
    }

    @Test (expected = MatchException.class)
    public void retirePlayerNotInMatchThrowsException() throws MatchException {
        Match match = new Match();
        match.setState(MatchState.IN_PROGRESS);
        match.setId(123456L);
        match.setUsers(Arrays.asList(user1, user2));

        User user3 = new User("Paola");

        user1.setId("ABC123");
        user2.setId("CBA321");
        user3.setId("BBB222");

        RetireDTO retireDTO = new RetireDTO();
        retireDTO.setPlayerId("BBB222");

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));
        Mockito.when(userRepository.findById("BBB222")).thenReturn(java.util.Optional.of(user3));

        matchService.retireFromMatch("123456", retireDTO);
    }

    @Test (expected = MatchException.class)
    public void retireFinishedMatchThrowsException() throws MatchException {
        Match match = new Match();
        match.setState(MatchState.FINISHED);
        MatchConfiguration config = new MatchConfiguration();
        match.setId(123456L);
        match.setConfig(config);
        match.setUsers(Arrays.asList(user1, user2));
        config.setPlayersTurns(new LinkedList<User>(Arrays.asList(user1, user2)));

        user1.setId("ABC123");
        user2.setId("CBA321");

        RetireDTO retireDTO = new RetireDTO();
        retireDTO.setPlayerId("ABC123");

        Mockito.when(matchRepository.findById(123456L)).thenReturn(java.util.Optional.of(match));
        Mockito.when(userRepository.findById("ABC123")).thenReturn(java.util.Optional.of(user1));

        matchService.retireFromMatch("123456", retireDTO);
    }

    @Test
    public void getStatisticsForMatchesByDatesOK() throws MatchException {
        Match match = new Match();
        match.setId(1234L);
        match.setState(MatchState.CREATED);
        match.setDate(LocalDateTime.of(2020, 9, 10, 0, 0));

        Match match2 = new Match();
        match2.setId(1235L);
        match2.setState(MatchState.CANCELLED);
        match2.setDate(LocalDateTime.of(2020, 9, 11, 0, 0));

        Match match3 = new Match();
        match3.setId(1236L);
        match3.setState(MatchState.IN_PROGRESS);
        match3.setDate(LocalDateTime.of(2020, 9, 12, 0, 0));

        Match match4 = new Match();
        match4.setId(1237L);
        match4.setState(MatchState.IN_PROGRESS);
        match4.setDate(LocalDateTime.of(2020, 9, 13, 0, 0));

        Match match5 = new Match();
        match5.setId(1238L);
        match5.setState(MatchState.FINISHED);
        match5.setDate(LocalDateTime.of(2020, 9, 9, 0, 0));

        Mockito.when(matchRepository.getMatches()).thenReturn(Arrays.asList(match, match2, match3, match4, match5));
        LocalDate dateFrom = LocalDate.of(2020, 9, 10);
        LocalDate dateTo = LocalDate.of(2020, 9, 20);

        MatchesStatisticsDTO dto = matchService.getStatisticsForMatches(dateFrom.format(DateTimeFormatter.ISO_LOCAL_DATE), dateTo.format(DateTimeFormatter.ISO_LOCAL_DATE));

        assertEquals(1, dto.getCreatedMatches());
        assertEquals(1, dto.getCancelledMatches());
        assertEquals(2, dto.getInProgressMatches());
        assertEquals(0, dto.getFinishedMatches());
    }

    @Test
    public void getStatisticsForMatchesDatesNotProvidedOK() throws MatchException {
        Match match = new Match();
        match.setId(1234L);
        match.setState(MatchState.CREATED);
        match.setDate(LocalDateTime.of(2020, 9, 10, 0, 0));

        Match match2 = new Match();
        match2.setId(1235L);
        match2.setState(MatchState.CANCELLED);
        match2.setDate(LocalDateTime.of(2020, 9, 11, 0, 0));

        Match match3 = new Match();
        match3.setId(1236L);
        match3.setState(MatchState.IN_PROGRESS);
        match3.setDate(LocalDateTime.of(2020, 9, 12, 0, 0));

        Match match4 = new Match();
        match4.setId(1237L);
        match4.setState(MatchState.IN_PROGRESS);
        match4.setDate(LocalDateTime.of(2020, 9, 13, 0, 0));

        Match match5 = new Match();
        match5.setId(1238L);
        match5.setState(MatchState.FINISHED);
        match5.setDate(LocalDateTime.of(2020, 9, 9, 0, 0));

        Mockito.when(matchRepository.getMatches()).thenReturn(Arrays.asList(match, match2, match3, match4, match5));

        MatchesStatisticsDTO dto = matchService.getStatisticsForMatches(null, null);

        assertEquals(1, dto.getCreatedMatches());
        assertEquals(1, dto.getCancelledMatches());
        assertEquals(2, dto.getInProgressMatches());
        assertEquals(1, dto.getFinishedMatches());
    }
}