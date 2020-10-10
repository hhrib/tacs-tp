package net.tacs.game.model.dto;

import java.util.Objects;

public class ProvinceDTOResponse implements Comparable<ProvinceDTOResponse> {

    private Long id;

    private String name;

    public ProvinceDTOResponse() {

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
        ProvinceDTOResponse that = (ProvinceDTOResponse) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(ProvinceDTOResponse otherBean) {
        return this.name.compareTo(otherBean.getName());
    }
}
