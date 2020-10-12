package net.tacs.game.services;

import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.model.UserStats;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> findAll() throws Exception;

    public User getUserById(Long id);

    public User getUserByUserName(String userName);

    public UserStats getUserStatistics(String username);

    public void setWinnerAndLosersStats(Match match);
}
