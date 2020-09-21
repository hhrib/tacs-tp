package net.tacs.game.model.bean;

import java.util.Comparator;
import java.util.Objects;

public class ProvinceBeanResponse implements Comparable<ProvinceBeanResponse> {

    private Long id;

    private String name;

    public ProvinceBeanResponse() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProvinceBeanResponse that = (ProvinceBeanResponse) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(ProvinceBeanResponse otherBean) {
        return this.name.compareTo(otherBean.getName());
    }
}
