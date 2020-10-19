package net.tacs.game.repositories;

import net.tacs.game.model.UserStats;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component("userStatisticsRepository")
public class UserStatisticsRepository {
    private static Map</*id*/String, UserStats> userStatistics = new HashMap<>();

    public UserStats getById(String id)
    {
        return userStatistics.get(id);
    }

    public Collection<UserStats> getAll()
    {
        return userStatistics.values();
    }

    public void addNewUserStats(String id, String username)
    {
        UserStats userStats = new UserStats();
        userStats.setUsername(username);
        userStatistics.put(id, userStats);
    }

    public boolean contains(String username)
    {
        return userStatistics.containsKey(username);
    }
}
