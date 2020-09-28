package net.tacs.game.model.dto;

import java.util.Objects;

public class MoveGauchosDTO {

    Long matchId;

    Integer idOriginMuni;

    Integer idDestinyMuni;

    Integer qty;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Integer getIdOriginMuni() {
        return idOriginMuni;
    }

    public void setIdOriginMuni(Integer idOriginMuni) {
        this.idOriginMuni = idOriginMuni;
    }

    public Integer getIdDestinyMuni() {
        return idDestinyMuni;
    }

    public void setIdDestinyMuni(Integer idDestinyMuni) {
        this.idDestinyMuni = idDestinyMuni;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveGauchosDTO that = (MoveGauchosDTO) o;
        return matchId.equals(that.matchId) &&
                idOriginMuni.equals(that.idOriginMuni) &&
                idDestinyMuni.equals(that.idDestinyMuni) &&
                qty.equals(that.qty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId, idOriginMuni, idDestinyMuni, qty);
    }

    @Override
    public String toString() {
        return "MoveGauchosBean{" +
                "matchId=" + matchId +
                ", idOriginMuni=" + idOriginMuni +
                ", idDestinyMuni=" + idDestinyMuni +
                ", qty=" + qty +
                '}';
    }

}
