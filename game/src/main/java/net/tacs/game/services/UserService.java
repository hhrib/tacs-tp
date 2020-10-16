package net.tacs.game.services;

import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.model.UserStats;
import net.tacs.game.model.dto.Scoreboard;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> findAll() throws Exception;

    public User getUserById(String id);

    public User getUserByUserName(String userName);

    public UserStats getUserStatistics(String username);

    public Scoreboard getScoreboard();

    public void setWinnerAndLosersStats(Match match);
}
