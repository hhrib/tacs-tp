package net.tacs.game.service;

import static org.junit.Assert.*;

import net.tacs.game.model.*;
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

    @Before
    public void setUp()
    {
        user1 = new User("Pepe");
        user2 = new User("Paula");
    }

    @Test
    public void setWinnerAndLosersOK()
    {
        Match match = new Match();
        match.setUsers(Arrays.asList(user1, user2));
        match.setWinner(user2);

        UserStats pepeStats = new UserStats();
        pepeStats.setMatchesWon(5);
        pepeStats.setMatchesLost(68);
        UserStats paulaStats = new UserStats();
        paulaStats.setMatchesWon(9);
        paulaStats.setMatchesLost(0);

        Mockito.when(userStatisticsRepository.getByUsername("Pepe")).thenReturn(pepeStats);
        Mockito.when(userStatisticsRepository.getByUsername("Paula")).thenReturn(paulaStats);

        userService.setWinnerAndLosersStats(match);

        assertEquals(10, paulaStats.getMatchesWon().intValue());
        assertEquals(0, paulaStats.getMatchesLost().intValue());
        assertEquals(5, pepeStats.getMatchesWon().intValue());
        assertEquals(69, pepeStats.getMatchesLost().intValue());
    }
}
