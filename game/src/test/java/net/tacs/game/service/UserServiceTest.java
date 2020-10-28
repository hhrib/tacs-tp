package net.tacs.game.service;

import static org.junit.Assert.*;

import net.tacs.game.model.*;
import net.tacs.game.model.dto.Scoreboard;
import net.tacs.game.repositories.UserStatisticsRepository;
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
}
