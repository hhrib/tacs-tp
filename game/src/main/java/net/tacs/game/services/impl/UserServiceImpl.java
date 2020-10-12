package net.tacs.game.services.impl;

import net.tacs.game.GameApplication;
import net.tacs.game.mapper.AuthUserToUserMapper;
import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.model.UserStats;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.repositories.UserStatisticsRepository;
import net.tacs.game.services.SecurityProviderService;
import net.tacs.game.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //TODO Ir a buscar al mapa que persiste en memoria para primeras entregas.
    @Override
    public User getUserById(Long id) {
        return null;
    }

    //TODO Ir a buscar al mapa que persiste en memoria para primeras entregas.
    @Override
    public User getUserByUserName(String userName) {
        return null;
    }

    public UserStats getUserStatistics(String username){
        return userStatisticsRepository.getByUsername(username);
    }

    public void setWinnerAndLosersStats(Match match)
    {
        User winner = match.getWinner();
        List<User> losers = new ArrayList<>(match.getUsers());

        losers.remove(winner);

        if(!userStatisticsRepository.contains(winner.getUsername()))
            userStatisticsRepository.addNewUserStats(winner.getUsername());
        UserStats winnerStats = userStatisticsRepository.getByUsername(winner.getUsername());
        winnerStats.addMatchesWon();

        for (User aUser : losers) {
            if(!userStatisticsRepository.contains(aUser.getUsername()))
                userStatisticsRepository.addNewUserStats(aUser.getUsername());
            UserStats loserStats = userStatisticsRepository.getByUsername(aUser.getUsername());
            loserStats.addMatchesLost();
        }
    }
}
