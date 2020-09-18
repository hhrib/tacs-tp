package net.tacs.game.model;

public class MatchConfiguration {
    public double minDist;
    public double maxDist;
    public double minHeight;
    public double maxHeight;
    public double multDefense = 1.25D;
    public int initialGauchos = 3000;

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

    public int getInitialGauchos() {
        return initialGauchos;
    }
}
