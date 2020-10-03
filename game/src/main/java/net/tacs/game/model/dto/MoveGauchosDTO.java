package net.tacs.game.model.dto;

import java.util.Objects;

public class MoveGauchosDTO {

    Integer idOriginMuni;

    Integer idDestinyMuni;

    Integer qty;

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
        return idOriginMuni.equals(that.idOriginMuni) &&
                idDestinyMuni.equals(that.idDestinyMuni) &&
                qty.equals(that.qty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOriginMuni, idDestinyMuni, qty);
    }

    @Override
    public String toString() {
        return "MoveGauchosBean{" +
                ", idOriginMuni=" + idOriginMuni +
                ", idDestinyMuni=" + idDestinyMuni +
                ", qty=" + qty +
                '}';
    }

}
