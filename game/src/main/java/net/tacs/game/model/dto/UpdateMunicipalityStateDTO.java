package net.tacs.game.model.dto;

import net.tacs.game.model.enums.MunicipalityState;

import java.util.Objects;

public class UpdateMunicipalityStateDTO {

    MunicipalityState newState;

    public MunicipalityState getNewState() {
        return newState;
    }

    public void setNewState(MunicipalityState newState) {
        this.newState = newState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateMunicipalityStateDTO that = (UpdateMunicipalityStateDTO) o;
        return newState == that.newState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(newState);
    }

    @Override
    public String toString() {
        return "UpdateMunicipalityStateDTO{" +
                "newState=" + newState +
                '}';
    }
}
