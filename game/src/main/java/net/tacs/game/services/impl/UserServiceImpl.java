package net.tacs.game.services.impl;

import net.tacs.game.GameApplication;
import net.tacs.game.exceptions.UserNotFoundException;
import net.tacs.game.mapper.AuthUserToUserMapper;
import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.model.UserStats;
import net.tacs.game.model.dto.Scoreboard;
import net.tacs.game.model.enums.MatchState;
import net.tacs.game.repositories.MatchRepository;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.repositories.UserStatisticsRepository;
import net.tacs.game.services.SecurityProviderService;
import net.tacs.game.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SecurityProviderService securityProviderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatisticsRepository userStatisticsRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Override
    public List<User> findAll() throws Exception {
        //Se va temporalmente a api de Auth0 hasta que resolvamos el Webhook que nos avise del registro de un nuevo usuario
        return AuthUserToUserMapper.mapUsers(securityProviderService.getUsers(GameApplication.getToken()));
        //return getUsers();
    }

    @Override
    public List<User> findAllAvailable() throws Exception {
        //Se va temporalmente a api de Auth0 hasta que resolvamos el Webhook que nos avise del registro de un nuevo usuario
        List<User> allUsers = AuthUserToUserMapper.mapUsers(securityProviderService.getUsers(GameApplication.getToken()));
        List<Match> allMatches = matchRepository.findAll();

        if(allMatches.isEmpty())
        {
            return allUsers;
        }

        List<Match> matchesInProgress = allMatches.stream().filter(m -> m.getState() != MatchState.FINISHED).collect(Collectors.toList());
        return allUsers.stream().filter(user -> user.isAvailable(matchesInProgress)).collect(Collectors.toList());
    }

    public Scoreboard getScoreboard()
    {
        Scoreboard scoreboard = new Scoreboard();

        scoreboard.fillAndSort(userStatisticsRepository.findAll());

        return scoreboard;
    }

    public void setWinnerAndLosersStats(Match match)
    {
        User winner = match.getWinner();
        List<User> losers = new ArrayList<>(match.getUsers());

        losers.remove(winner);

        UserStats winnerStats = userStatisticsRepository.getById(winner.getId());
        if(winnerStats == null)
            winnerStats = new UserStats(winner.getId(), winner.getUsername());

        winnerStats.addMatchesWon();
        userStatisticsRepository.save(winnerStats);

        for (User aUser : losers) {
            UserStats loserStats = userStatisticsRepository.getById(aUser.getId());
            if(loserStats == null)
                loserStats = new UserStats(aUser.getId(), aUser.getUsername());

            loserStats.addMatchesLost();
            userStatisticsRepository.save(loserStats);
        }
    }
}
