package net.tacs.game.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userStats")
public class UserStats {
    private String id;
    private String username;
    private Integer matchesWon = 0;
    private Integer matchesLost = 0;

    public UserStats(){}

    public UserStats(String username)
    {
        this.username = username;
    }

    public UserStats(String id, String username)
    {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public Integer getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
    }

    public void addMatchesWon(){
        this.matchesWon++;
    }

    public Integer getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(Integer matchesLost) {
        this.matchesLost = matchesLost;
    }

    public void addMatchesLost(){
        this.matchesLost++;
    }
}
