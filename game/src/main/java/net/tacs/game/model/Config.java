package net.tacs.game.model;

public class Config {

    public Config() {
    }

    private Match match;
    private Double defenseMultiplier;
    private Double gauchosDefMult;
    private Double gauchosProMult;
    private Double heightMultiplier;
    private Double distanceMultiplier;

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Double getDefenseMultiplier() {
        return defenseMultiplier;
    }

    public void setDefenseMultiplier(Double defenseMultiplier) {
        this.defenseMultiplier = defenseMultiplier;
    }

    public Double getGauchosDefMult() {
        return gauchosDefMult;
    }

    public void setGauchosDefMult(Double gauchosDefMult) {
        this.gauchosDefMult = gauchosDefMult;
    }

    public Double getGauchosProMult() {
        return gauchosProMult;
    }

    public void setGauchosProMult(Double gauchosProMult) {
        this.gauchosProMult = gauchosProMult;
    }

    public Double getHeightMultiplier() {
        return heightMultiplier;
    }

    public void setHeightMultiplier(Double heightMultiplier) {
        this.heightMultiplier = heightMultiplier;
    }

    public Double getDistanceMultiplier() {
        return distanceMultiplier;
    }

    public void setDistanceMultiplier(Double distanceMultiplier) {
        this.distanceMultiplier = distanceMultiplier;
    }
}
