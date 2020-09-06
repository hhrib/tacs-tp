package net.tacs.game.model;

public class Config {

    public Config() {
    }

    private Match match;
    private float defenseMultiplier;
    private float gauchosDefMult;
    private float gauchosProMult;
    private float heightMultiplier;
    private float distanceMultiplier;

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public float getDefenseMultiplier() {
        return defenseMultiplier;
    }

    public void setDefenseMultiplier(float defenseMultiplier) {
        this.defenseMultiplier = defenseMultiplier;
    }

    public float getGauchosDefMult() {
        return gauchosDefMult;
    }

    public void setGauchosDefMult(float gauchosDefMult) {
        this.gauchosDefMult = gauchosDefMult;
    }

    public float getGauchosProMult() {
        return gauchosProMult;
    }

    public void setGauchosProMult(float gauchosProMult) {
        this.gauchosProMult = gauchosProMult;
    }

    public float getHeightMultiplier() {
        return heightMultiplier;
    }

    public void setHeightMultiplier(float heightMultiplier) {
        this.heightMultiplier = heightMultiplier;
    }

    public float getDistanceMultiplier() {
        return distanceMultiplier;
    }

    public void setDistanceMultiplier(float distanceMultiplier) {
        this.distanceMultiplier = distanceMultiplier;
    }
}
