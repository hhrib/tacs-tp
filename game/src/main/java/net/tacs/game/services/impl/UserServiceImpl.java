package net.tacs.game.services.impl;

import net.tacs.game.GameApplication;
import net.tacs.game.exceptions.UserNotFoundException;
import net.tacs.game.mapper.AuthUserToUserMapper;
import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.model.UserStats;
import net.tacs.game.model.dto.Scoreboard;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.repositories.UserStatisticsRepository;
import net.tacs.game.services.SecurityProviderService;
import net.tacs.game.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SecurityProviderService securityProviderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatisticsRepository userStatisticsRepository;

    @Override
    public List<User> findAll() throws Exception {
        //Se va temporalmente a api de Auth0 hasta que resolvamos el Webhook que nos avise del registro de un nuevo usuario
        return AuthUserToUserMapper.mapUsers(securityProviderService.getUsers(GameApplication.getToken()));
//        return getUsers();
    }

    @Override
    public User getUserById(String id)
    {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User getUserByUserName(String userName)
    {
        Optional<User> userOptional = userRepository.findByUsername(userName);
        return userOptional.orElseThrow(() -> new UserNotFoundException(userName));
    }

    public UserStats getUserStatistics(String id){
        return userStatisticsRepository.getById(id);
    }

    public Scoreboard getScoreboard()
    {
        Scoreboard scoreboard = new Scoreboard();

        scoreboard.fillAndSort(userStatisticsRepository.getAll());

        return scoreboard;
    }

    public void setWinnerAndLosersStats(Match match)
    {
        User winner = match.getWinner();
        List<User> losers = new ArrayList<>(match.getUsers());

        losers.remove(winner);

        if(!userStatisticsRepository.contains(winner.getId()))
            userStatisticsRepository.addNewUserStats(winner.getId(), winner.getUsername());
        UserStats winnerStats = userStatisticsRepository.getById(winner.getId());
        winnerStats.addMatchesWon();

        for (User aUser : losers) {
            if(!userStatisticsRepository.contains(aUser.getId()))
                userStatisticsRepository.addNewUserStats(aUser.getId(), winner.getUsername());
            UserStats loserStats = userStatisticsRepository.getById(aUser.getId());
            loserStats.addMatchesLost();
        }
    }
}
