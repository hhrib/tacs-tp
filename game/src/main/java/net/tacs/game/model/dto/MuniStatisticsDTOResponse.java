package net.tacs.game.model.dto;

//import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.model.interfaces.MunicipalityState;

import java.util.Objects;

public class MuniStatisticsDTOResponse {

    private Integer muniId;

    private MunicipalityState state;

    private Integer gauchosQty;

    private boolean blocked;

    public Integer getMuniId() {
        return muniId;
    }

    public void setMuniId(Integer muniId) {
        this.muniId = muniId;
    }

    public MunicipalityState getState() {
        return state;
    }

    public void setState(MunicipalityState state) {
        this.state = state;
    }

    public Integer getGauchosQty() {
        return gauchosQty;
    }

    public void setGauchosQty(Integer gauchosQty) {
        this.gauchosQty = gauchosQty;
    }

    public boolean getBlocked() {return blocked;}

    public void setBlocked(boolean bValue) {this.blocked = bValue;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MuniStatisticsDTOResponse that = (MuniStatisticsDTOResponse) o;
        return muniId.equals(that.muniId) &&
                state == that.state &&
                gauchosQty.equals(that.gauchosQty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(muniId, state, gauchosQty);
    }

    @Override
    public String toString() {
        return "MuniStatisticDTOResponse{" +
                "muniId=" + muniId +
                ", state=" + state +
                ", gauchosQty=" + gauchosQty +
                '}';
    }
}
