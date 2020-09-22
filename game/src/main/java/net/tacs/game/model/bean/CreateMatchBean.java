package net.tacs.game.model.bean;

import java.util.List;
import java.util.Objects;

public class CreateMatchBean {

    private Long provinceId;
    private Integer municipalitiesQty;
    private List<String> userIds;
    private List<Double> configs;

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

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<Double> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Double> configs) {
        this.configs = configs;
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
