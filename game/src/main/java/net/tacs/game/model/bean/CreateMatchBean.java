package net.tacs.game.model.bean;

import java.util.List;
import java.util.Objects;

public class CreateMatchBean {

    private Long provinceId;
    private Integer municipalitiesQty;
    private List<Long> userIds;

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getMunicipalitiesQty() {
        return municipalitiesQty;
    }

    public void setMunicipalitiesQty(Integer municipalitiesQty) {
        this.municipalitiesQty = municipalitiesQty;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    @Override
    public String toString() {
        return "CreateMatchBean{" +
                "provinceId=" + provinceId +
                ", municipalitiesQty=" + municipalitiesQty +
                ", userIds=" + userIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateMatchBean that = (CreateMatchBean) o;
        return provinceId.equals(that.provinceId) &&
                municipalitiesQty.equals(that.municipalitiesQty) &&
                userIds.equals(that.userIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provinceId, municipalitiesQty, userIds);
    }
}
