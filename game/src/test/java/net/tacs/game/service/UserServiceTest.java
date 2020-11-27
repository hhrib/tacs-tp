package net.tacs.game.service;

import static org.junit.Assert.*;

import net.tacs.game.GameApplication;
import net.tacs.game.model.*;
import net.tacs.game.model.dto.Scoreboard;
import net.tacs.game.model.opentopodata.auth.AuthUserResponse;
import net.tacs.game.repositories.MatchRepository;
import net.tacs.game.repositories.UserStatisticsRepository;
import net.tacs.game.services.SecurityProviderService;
import net.tacs.game.services.UserService;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserStatisticsRepository userStatisticsRepository;

    @MockBean
    private SecurityProviderService securityProviderService;

    @MockBean
    private MatchRepository matchRepository;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp()
    {
        user1 = new User("Pepe");
        user1.setId("123456");
        user2 = new User("Paula");
        user2.setId("123457");
        user3 = new User("Paola");
        user3.setId("123458");
    }

    @Test
    public void setWinnerAndLosersOK()
    {
        Match match = new Match();
        match.setUsers(Arrays.asList(user1, user2));
        match.setWinner(user2);

        UserStats pepeStats = new UserStats("Pepe");
        pepeStats.setMatchesWon(5);
        pepeStats.setMatchesLost(68);
        UserStats paulaStats = new UserStats("Paula");
        paulaStats.setMatchesWon(9);
        paulaStats.setMatchesLost(0);

        Mockito.when(userStatisticsRepository.getById("123456")).thenReturn(pepeStats);
        Mockito.when(userStatisticsRepository.getById("123457")).thenReturn(paulaStats);

        userService.setWinnerAndLosersStats(match);

        assertEquals(10, paulaStats.getMatchesWon().intValue());
        assertEquals(0, paulaStats.getMatchesLost().intValue());
        assertEquals(5, pepeStats.getMatchesWon().intValue());
        assertEquals(69, pepeStats.getMatchesLost().intValue());
    }

    @Test
    public void getScoreboardOK()
    {
        UserStats pepeStats = new UserStats("Pepe");
        pepeStats.setMatchesWon(5);
        pepeStats.setMatchesLost(68);
        UserStats paolaStats = new UserStats("Paola");
        paolaStats.setMatchesWon(2);
        paolaStats.setMatchesLost(7);
        UserStats paulaStats = new UserStats("Paula");
        paulaStats.setMatchesWon(9);
        paulaStats.setMatchesLost(0);

        List<UserStats> statsList = Arrays.asList(pepeStats, paolaStats, paulaStats);

        Mockito.when(userStatisticsRepository.findAll()).thenReturn(statsList);

        Scoreboard scoreboard = userService.getScoreboard();

        assertEquals("Paula", scoreboard.getByIndex(0).getUsername());
        assertEquals("Pepe", scoreboard.getByIndex(1).getUsername());
        assertEquals("Paola", scoreboard.getByIndex(2).getUsername());
    }

    @Test
    public void getAllUsersAvailableNoMatches() throws Exception {
        AuthUserResponse userResponse1 = new AuthUserResponse();
        userResponse1.setUserId("123456");
        userResponse1.setNickname("Pepe");

        AuthUserResponse userResponse2 = new AuthUserResponse();
        userResponse2.setUserId("123457");
        userResponse2.setNickname("Paula");

        AuthUserResponse userResponse3 = new AuthUserResponse();
        userResponse3.setUserId("123458");
        userResponse3.setNickname("Paola");

        Mockito.when(securityProviderService.getUsers(GameApplication.getToken()))
                .thenReturn(Arrays.asList(userResponse1, userResponse2, userResponse3));

        Mockito.when(matchRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> availableUsers = userService.findAllAvailable();

        assertTrue(availableUsers.contains(user1));
        assertTrue(availableUsers.contains(user2));
        assertTrue(availableUsers.contains(user3));
    }

    @Test
    public void getAllUsersAvailableWithMatches() throws Exception {
        AuthUserResponse userResponse1 = new AuthUserResponse();
        userResponse1.setUserId("123456");
        userResponse1.setNickname("Pepe");

        AuthUserResponse userResponse2 = new AuthUserResponse();
        userResponse2.setUserId("123457");
        userResponse2.setNickname("Paula");

        AuthUserResponse userResponse3 = new AuthUserResponse();
        userResponse3.setUserId("123458");
        userResponse3.setNickname("Paola");

        Match match = new Match();
        match.setUsers(Arrays.asList(user3));

        Mockito.when(securityProviderService.getUsers(GameApplication.getToken()))
                .thenReturn(Arrays.asList(userResponse1, userResponse2, userResponse3));

        Mockito.when(matchRepository.findAll()).thenReturn(Collections.singletonList(match));

        List<User> availableUsers = userService.findAllAvailable();

        assertTrue(availableUsers.contains(user1));
        assertTrue(availableUsers.contains(user2));
        assertFalse(availableUsers.contains(user3));
    }
}
