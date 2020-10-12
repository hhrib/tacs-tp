package net.tacs.game.repositories;

import net.tacs.game.model.UserStats;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("userStatisticsRepository")
public class UserStatisticsRepository {
    private static Map<String, UserStats> userStatistics = new HashMap<>();

    public UserStats getByUsername(String username)
    {
        return userStatistics.get(username);
    }

    public void addNewUserStats(String username)
    {
        userStatistics.put(username, new UserStats());
    }

    public boolean contains(String username)
    {
        return userStatistics.containsKey(username);
    }
}
