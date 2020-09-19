package net.tacs.game.model;

public class MatchConfiguration {
    private double minDist;
    private double maxDist;
    private double minHeight;
    private double maxHeight;
    private double multDefense = 1.25D;
    private double multGauchosProduction = 15;
    private double multGauchosDefense = 10;
    private double multHeight = 2;
    private double multDistance = 2;
    private int initialGauchos = 3000;

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
}
