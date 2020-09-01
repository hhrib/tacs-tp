package net.tacs.game.model;

import java.util.Objects;

public class Municipality {

    public Municipality() {
    }

    public Municipality(String name) {
        this.name = name;
    }

    private String name;
    private Province province;
    private User user;
    private MunicipalityState state;
    private int gauchos;
    private float height;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MunicipalityState getState() {
        return state;
    }

    public void setState(MunicipalityState state) {
        this.state = state;
    }

    public int getGauchos() {
        return gauchos;
    }

    public void setGauchos(int gauchos) {
        this.gauchos = gauchos;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }


    @Override
    public String toString() {
        return "Municipality{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Municipality municipality = (Municipality) o;
        return Objects.equals(name, municipality.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
