package net.tacs.game.model;

public class UserStats {
    private Integer matchesWon = 0;
    private Integer matchesLost = 0;

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
