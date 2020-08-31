package net.tacs.game.model;

import java.util.Objects;

public class Province {

    private String name;

    public String getName() {
        return name;
    }

    public Province() {
    }

    public Province(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Province{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Province province = (Province) o;
        return Objects.equals(name, province.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
