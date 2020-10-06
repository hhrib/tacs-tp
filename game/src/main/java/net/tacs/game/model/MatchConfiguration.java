package net.tacs.game.model;

import java.util.ArrayList;
import java.util.List;

public class MatchConfiguration {
    private double minDist; //<-- Se calculan
    private double maxDist; //<-- Se calculan
    private double minHeight; //<-- Se calculan
    private double maxHeight; //<-- Se calculan
    private double multGauchosProduction = 15;
    private double multGauchosDefense = 10;
    private double multDistance = 2;
    private double multHeight = 2;
    private double multDefense = 1.25D;
    private int initialGauchos = 3000;

    private List<User> playersTurns = new ArrayList<>();

    public void setMinDist(double minDist) {
        this.minDist = minDist;
    }

    public void setMaxDist(double maxDist) {
        this.maxDist = maxDist;
    }

    public void setMinHeight(double minHeight) {
        this.minHeight = minHeight;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setMultDefense(double multDefense) {
        this.multDefense = multDefense;
    }

    public void setMultGauchosProduction(double multGauchosProduction) {
        this.multGauchosProduction = multGauchosProduction;
    }

    public void setMultGauchosDefense(double multGauchosDefense) {
        this.multGauchosDefense = multGauchosDefense;
    }

    public void setMultHeight(double multHeight) {
        this.multHeight = multHeight;
    }

    public void setMultDistance(double multDistance) {
        this.multDistance = multDistance;
    }

    public void setInitialGauchos(int initialGauchos) {
        this.initialGauchos = initialGauchos;
    }

    public double getMinDist() {
        return minDist;
    }

    public double getMaxDist() {
        return maxDist;
    }

    public double getMinHeight() {
        return minHeight;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public double getMultDefense() {
        return multDefense;
    }

    public double getMultGauchosProduction() {
        return multGauchosProduction;
    }

    public double getMultGauchosDefense() {
        return multGauchosDefense;
    }

    public double getMultHeight() {
        return multHeight;
    }

    public double getMultDistance() {
        return multDistance;
    }

    public int getInitialGauchos() {
        return initialGauchos;
    }

    public List<User> getPlayersTurns() {
        return playersTurns;
    }

    public void setPlayersTurns(List<User> playerTurns) {
        this.playersTurns = playerTurns;
    }

    public User setNextPlayerTurn(String playerId) {
        for(User aUser : playersTurns)
        {
            if(aUser.getId().equals(playerId))
            {
                int index = playersTurns.indexOf(aUser);

                //es el ultimo de la lista?
                if(index == (playersTurns.size() - 1))
                {
                    return playersTurns.get(0);
                }
                else
                {
                    return playersTurns.get(index + 1);
                }
            }
        }

        return null;
    }

    public void removePlayer(User player) {
        playersTurns.remove(player);
    }
}
