package net.tacs.game.model;

public class UserStats {
    private String username;
    private Integer matchesWon = 0;
    private Integer matchesLost = 0;

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
