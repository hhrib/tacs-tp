package net.tacs.game.model.dto;

import net.tacs.game.model.UserStats;

import java.util.*;

public class Scoreboard {

    private List<UserStats> scoreboard;

    public UserStats getByIndex(int index)
    {
        return scoreboard.get(index);
    }

    public void fillAndSort(Collection<UserStats> userStats)
    {
        scoreboard = new ArrayList<>(userStats);
        Collections.sort(scoreboard, new SortByMatchesWon());
    }

    class SortByMatchesWon implements Comparator<UserStats>
    {
        public int compare(UserStats a, UserStats b)
        {
            return b.getMatchesWon() - a.getMatchesWon();
        }
    }

    public List<UserStats> getScoreboard() {
        return scoreboard;
    }
}
