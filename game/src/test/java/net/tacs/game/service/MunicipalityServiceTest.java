package net.tacs.game.service;

import net.tacs.game.GameApplication;
import net.tacs.game.exceptions.MatchException;
import net.tacs.game.exceptions.MatchNotPlayerTurnException;
import net.tacs.game.exceptions.MatchNotStartedException;
import net.tacs.game.model.*;
import net.tacs.game.model.dto.AttackMuniDTO;
import net.tacs.game.model.dto.AttackResultDTO;
import net.tacs.game.model.dto.MoveGauchosDTO;
import net.tacs.game.model.enums.MatchState;
import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.repositories.MatchRepository;
import net.tacs.game.services.MatchService;
import net.tacs.game.services.MunicipalityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class MunicipalityServiceTest {

    @Autowired
    private MunicipalityService municipalityService;

    @MockBean
    private MatchService matchService;

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
    public void attackSuccessfullMunicipality() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        match.setId(111111L);
        match.setState(MatchState.IN_PROGRESS);
        match.setMap(buenosAires);
        match.getMap().setMunicipalities(Arrays.asList(Lanus, Avellaneda, Quilmes));

        Mockito.when(matchService.getMatchById("111111")).thenReturn(match);

        MatchConfiguration Config = new MatchConfiguration();
        match.setConfig(Config);
        match.setUsers(Arrays.asList(user1, user2));
        match.setTurnPlayer(user1);
        Config.setMaxHeight(2000D);
        Config.setMinHeight(1000D);
        Config.setMaxDist(20D);
        Config.setMinDist(10D);
        Config.setPlayersTurns(Arrays.asList(user1, user2));

        Centroide lanusCentroide = Mockito.mock(Centroide.class);
        Centroide avellanedaCentroide = new Centroide();

        Lanus.setId(999999);
        Lanus.setOwner(user1);
        Avellaneda.setId(888888);
        Avellaneda.setOwner(user2);
        Quilmes.setId(777777);
        Quilmes.setOwner(user2);

        Lanus.setCentroide(lanusCentroide);
        Avellaneda.setCentroide(avellanedaCentroide);
        Mockito.when(lanusCentroide.getDistance(avellanedaCentroide)).thenReturn(15D);

        Lanus.setElevation(1500D);
        Avellaneda.setElevation(1500D);

        Lanus.setGauchosQty(300);
        Avellaneda.setGauchosQty(100);

        Avellaneda.setState(MunicipalityState.DEFENSE);

        AttackMuniDTO attackMuniDTO = new AttackMuniDTO();
        attackMuniDTO.setGauchosQty(250);
        attackMuniDTO.setMuniAttackingId(999999);
        attackMuniDTO.setMuniDefendingId(888888);

        AttackResultDTO attackResultDTO = municipalityService.attackMunicipality("111111", attackMuniDTO);

        assertEquals(1, attackResultDTO.getResult());
    }

    @Test
    public void attackRepelledMunicipality() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        match.setId(111111L);
        match.setState(MatchState.IN_PROGRESS);
        match.setMap(buenosAires);
        match.getMap().setMunicipalities(Arrays.asList(Lanus, Avellaneda));

        Mockito.when(matchService.getMatchById("111111")).thenReturn(match);

        MatchConfiguration Config = new MatchConfiguration();
        match.setConfig(Config);
        match.setUsers(Arrays.asList(user1, user2));
        match.setTurnPlayer(user1);
        Config.setMaxHeight(2000D);
        Config.setMinHeight(1000D);
        Config.setMaxDist(20D);
        Config.setMinDist(10D);
        Config.setPlayersTurns(Arrays.asList(user1, user2));

        Centroide lanusCentroide = Mockito.mock(Centroide.class);
        Centroide avellanedaCentroide = new Centroide();

        Lanus.setCentroide(lanusCentroide);
        Avellaneda.setCentroide(avellanedaCentroide);
        Mockito.when(lanusCentroide.getDistance(avellanedaCentroide)).thenReturn(15D);

        Lanus.setId(999999);
        Lanus.setOwner(user1);
        Avellaneda.setId(888888);
        Avellaneda.setOwner(user2);

        Lanus.setElevation(1500D);
        Avellaneda.setElevation(1500D);

        Lanus.setGauchosQty(100);
        Avellaneda.setGauchosQty(100);

        Avellaneda.setState(MunicipalityState.PRODUCTION);

        AttackMuniDTO attackMuniDTO = new AttackMuniDTO();
        attackMuniDTO.setGauchosQty(100);
        attackMuniDTO.setMuniAttackingId(999999);
        attackMuniDTO.setMuniDefendingId(888888);

        AttackResultDTO attackResultDTO = municipalityService.attackMunicipality("111111", attackMuniDTO);

        assertEquals(0, attackResultDTO.getResult());
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
    public void municipalityMoveGauchosOK() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        match.setId(1235L);
        match.setState(MatchState.IN_PROGRESS);
        match.setMap(buenosAires);
        match.setConfig(configuration);
        match.setTurnPlayer(user2);
        buenosAires.setMunicipalities(Arrays.asList(Lanus, Avellaneda, Quilmes));

        Lanus.setId(77777);
        Lanus.setOwner(user1);
        Avellaneda.setId(99999);
        Avellaneda.setOwner(user2);
        Quilmes.setId(88888);
        Quilmes.setOwner(user2);

        Lanus.setGauchosQty(3000);
        Avellaneda.setGauchosQty(2500);
        Quilmes.setGauchosQty(1500);

        Quilmes.setBlocked(false);

        Mockito.when(matchService.getMatchById("1235")).thenReturn(match);

        MoveGauchosDTO dto = new MoveGauchosDTO();
        dto.setIdOriginMuni(99999);
        dto.setIdDestinyMuni(88888);
        dto.setQty(500);

        municipalityService.moveGauchos("1235", dto);

        assertEquals(3000, Lanus.getGauchosQty());
        assertEquals(2000, Avellaneda.getGauchosQty());
        assertEquals(2000, Quilmes.getGauchosQty());
    }

    @Test (expected = MatchException.class)
    public void municipalityMoveGauchosBlocked() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        match.setId(1235L);
        match.setState(MatchState.IN_PROGRESS);
        match.setMap(buenosAires);
        match.setConfig(configuration);
        match.setTurnPlayer(user2);
        buenosAires.setMunicipalities(Arrays.asList(Avellaneda, Quilmes));

        Avellaneda.setId(99999);
        Avellaneda.setOwner(user2);
        Quilmes.setId(88888);
        Quilmes.setOwner(user2);

        Avellaneda.setGauchosQty(2500);
        Quilmes.setGauchosQty(1500);

        Quilmes.setBlocked(true);

        Mockito.when(matchService.getMatchById("1235")).thenReturn(match);

        MoveGauchosDTO dto = new MoveGauchosDTO();
        dto.setIdOriginMuni(99999);
        dto.setIdDestinyMuni(88888);
        dto.setQty(500);

        municipalityService.moveGauchos("1235",dto);
    }

    @Test (expected = MatchException.class)
    public void municipalityMoveGauchosDifferentOwnersThrowsException() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        match.setId(1235L);
        match.setState(MatchState.IN_PROGRESS);
        match.setMap(buenosAires);
        match.setConfig(configuration);
        match.setTurnPlayer(user2);
        buenosAires.setMunicipalities(Arrays.asList(Avellaneda, Quilmes));

        Avellaneda.setId(99999);
        Avellaneda.setOwner(user2);
        Quilmes.setId(88888);
        Quilmes.setOwner(user1);

        Avellaneda.setGauchosQty(2500);
        Quilmes.setGauchosQty(1500);

        Mockito.when(matchService.getMatchById("1235")).thenReturn(match);

        MoveGauchosDTO dto = new MoveGauchosDTO();
        dto.setIdOriginMuni(99999);
        dto.setIdDestinyMuni(88888);
        dto.setQty(500);

        municipalityService.moveGauchos("1235",dto);
    }

    @Test (expected = MatchNotPlayerTurnException.class)
    public void municipalityMoveGauchosNotPlayerTurnThrowsException() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        match.setId(1235L);
        match.setState(MatchState.IN_PROGRESS);
        match.setMap(buenosAires);
        match.setConfig(configuration);
        match.setTurnPlayer(user1);
        buenosAires.setMunicipalities(Arrays.asList(Avellaneda, Quilmes));

        Avellaneda.setId(99999);
        Avellaneda.setOwner(user2);
        Quilmes.setId(88888);
        Quilmes.setOwner(user2);

        Avellaneda.setGauchosQty(2500);
        Quilmes.setGauchosQty(1500);

        Mockito.when(matchService.getMatchById("1235")).thenReturn(match);

        MoveGauchosDTO dto = new MoveGauchosDTO();
        dto.setIdOriginMuni(99999);
        dto.setIdDestinyMuni(88888);
        dto.setQty(500);

        municipalityService.moveGauchos("1235",dto);
    }

    @Test
    public void allMunicipalitiesProduceGauchos()
    {
        Match match = new Match();
        match.setId(1235L);
        match.setState(MatchState.IN_PROGRESS);
        match.setMap(buenosAires);
        match.setConfig(configuration);
        buenosAires.setMunicipalities(Arrays.asList(Lanus, Avellaneda, Quilmes, Tigre, Matanza, Lomas));

        Lanus.setId(77777);
        Avellaneda.setId(99999);
        Quilmes.setId(88888);
        Tigre.setId(66666);
        Matanza.setId(55555);
        Lomas.setId(44444);

        Lanus.setGauchosQty(0);
        Avellaneda.setGauchosQty(1000);
        Quilmes.setGauchosQty(0);
        Tigre.setGauchosQty(10);
        Matanza.setGauchosQty(1200);
        Lomas.setGauchosQty(500);

        Lanus.setOwner(user1);
        Avellaneda.setOwner(user1);
        Quilmes.setOwner(user1);
        Tigre.setOwner(user2);
        Matanza.setOwner(user2);
        Lomas.setOwner(user2);

        configuration.setMaxHeight(2000D);
        configuration.setMinHeight(1000D);
        configuration.setMaxDist(20D);
        configuration.setMinDist(10D);

        Lanus.setElevation(1500D);
        Lanus.setState(MunicipalityState.DEFENSE);
        Avellaneda.setElevation(1500D);
        Avellaneda.setState(MunicipalityState.PRODUCTION);
        Quilmes.setElevation(1500D);
        Quilmes.setState(MunicipalityState.PRODUCTION);

        municipalityService.produceGauchos(match, user1);

        assertEquals(7, Lanus.getGauchosQty());
        assertEquals(1011, Avellaneda.getGauchosQty());
        assertEquals(11, Quilmes.getGauchosQty());
        assertEquals(10, Tigre.getGauchosQty());
        assertEquals(1200, Matanza.getGauchosQty());
        assertEquals(500, Lomas.getGauchosQty());
    }

    @Test
    public void attackFinishedMatch() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        match.setId(111111L);
        match.setState(MatchState.IN_PROGRESS);
        match.setMap(buenosAires);
        match.getMap().setMunicipalities(Arrays.asList(Lanus, Avellaneda));

        Mockito.when(matchService.getMatchById("111111")).thenReturn(match);

        MatchConfiguration Config = new MatchConfiguration();
        match.setConfig(Config);
        match.setUsers(Arrays.asList(user1, user2));
        match.setTurnPlayer(user1);
        Config.setMaxHeight(2000D);
        Config.setMinHeight(1000D);
        Config.setMaxDist(20D);
        Config.setMinDist(10D);
        Config.setPlayersTurns(new LinkedList<User>(Arrays.asList(user1, user2)));

        Centroide lanusCentroide = Mockito.mock(Centroide.class);
        Centroide avellanedaCentroide = new Centroide();

        Lanus.setId(999999);
        Lanus.setOwner(user1);
        Avellaneda.setId(888888);
        Avellaneda.setOwner(user2);

        Lanus.setCentroide(lanusCentroide);
        Avellaneda.setCentroide(avellanedaCentroide);
        Mockito.when(lanusCentroide.getDistance(avellanedaCentroide)).thenReturn(15D);

        Lanus.setElevation(1500D);
        Avellaneda.setElevation(1500D);

        Lanus.setGauchosQty(300);
        Avellaneda.setGauchosQty(100);

        Avellaneda.setState(MunicipalityState.DEFENSE);

        AttackMuniDTO attackMuniDTO = new AttackMuniDTO();
        attackMuniDTO.setGauchosQty(250);
        attackMuniDTO.setMuniAttackingId(999999);
        attackMuniDTO.setMuniDefendingId(888888);

        municipalityService.attackMunicipality("111111", attackMuniDTO);

        assertEquals(MatchState.FINISHED, match.getState());
        assertEquals(user1, match.getWinner());
    }

    @Test(expected = MatchException.class)
    public void attackSameOwnerMunicipalitiesThrowsException() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        match.setId(111111L);
        match.setState(MatchState.IN_PROGRESS);
        match.setMap(buenosAires);
        match.getMap().setMunicipalities(Arrays.asList(Lanus, Avellaneda));
        match.setTurnPlayer(user1);

        Mockito.when(matchService.getMatchById("111111")).thenReturn(match);

        Lanus.setId(999999);
        Lanus.setOwner(user1);
        Lanus.setBlocked(false);
        Avellaneda.setId(888888);
        Avellaneda.setOwner(user1);

        AttackMuniDTO attackMuniDTO = new AttackMuniDTO();
        attackMuniDTO.setGauchosQty(250);
        attackMuniDTO.setMuniAttackingId(999999);
        attackMuniDTO.setMuniDefendingId(888888);

        municipalityService.attackMunicipality("111111", attackMuniDTO);
    }

    @Test(expected = MatchException.class)
    public void attackBlockedMunicipalityThrowsException() throws MatchException, MatchNotPlayerTurnException, MatchNotStartedException {
        Match match = new Match();
        match.setId(111111L);
        match.setState(MatchState.IN_PROGRESS);
        match.setMap(buenosAires);
        match.getMap().setMunicipalities(Arrays.asList(Lanus, Avellaneda));
        match.setTurnPlayer(user1);

        Mockito.when(matchService.getMatchById("111111")).thenReturn(match);

        Lanus.setId(999999);
        Lanus.setOwner(user1);
        Lanus.setBlocked(true);
        Avellaneda.setId(888888);
        Avellaneda.setOwner(user1);

        AttackMuniDTO attackMuniDTO = new AttackMuniDTO();
        attackMuniDTO.setGauchosQty(250);
        attackMuniDTO.setMuniAttackingId(999999);
        attackMuniDTO.setMuniDefendingId(888888);

        municipalityService.attackMunicipality("111111", attackMuniDTO);
    }
}