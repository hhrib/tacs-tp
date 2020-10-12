package net.tacs.game.model.dto;

public class MatchesStatisticsDTO {
    private int createdMatches = 0;
    private int cancelledMatches = 0;
    private int inProgressMatches = 0;
    private int finishedMatches = 0;

    public int getCreatedMatches() {
        return createdMatches;
    }

    public void setCreatedMatches(int createdMatches) {
        this.createdMatches = createdMatches;
    }

    public void addCreatedMatched(){
        this.createdMatches++;
    }

    public int getCancelledMatches() {
        return cancelledMatches;
    }

    public void setCancelledMatches(int cancelledMatches) {
        this.cancelledMatches = cancelledMatches;
    }

    public void addCancelledMatches(){
        this.cancelledMatches++;
    }

    public int getInProgressMatches() {
        return inProgressMatches;
    }

    public void setInProgressMatches(int inProgressMatches) {
        this.inProgressMatches = inProgressMatches;
    }

    public void addInProgressMatches(){
        this.inProgressMatches++;
    }

    public int getFinishedMatches() {
        return finishedMatches;
    }

    public void setFinishedMatches(int finishedMatches) {
        this.finishedMatches = finishedMatches;
    }

    public void addFinishedMatches(){
        this.finishedMatches++;
    }
}
