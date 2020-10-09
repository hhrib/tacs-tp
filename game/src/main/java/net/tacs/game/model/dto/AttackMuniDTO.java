package net.tacs.game.model.dto;

public class AttackMuniDTO {
    private int muniAttackingId;
    private int muniDefendingId;
    private int gauchosQty;

    public int getMuniAttackingId() {
        return muniAttackingId;
    }

    public void setMuniAttackingId(int muniAttackingId) {
        this.muniAttackingId = muniAttackingId;
    }

    public int getMuniDefendingId() {
        return muniDefendingId;
    }

    public void setMuniDefendingId(int muniDefendingId) {
        this.muniDefendingId = muniDefendingId;
    }

    public int getGauchosQty() {
        return gauchosQty;
    }

    public void setGauchosQty(int gauchosQty) {
        this.gauchosQty = gauchosQty;
    }
}
