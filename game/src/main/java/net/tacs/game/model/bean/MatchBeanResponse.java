package net.tacs.game.model.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import net.tacs.game.model.User;
import net.tacs.game.model.enums.MatchState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class MatchBeanResponse {

    Long id;

    List<User> users;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    LocalDateTime date;

    MatchState state;

    String map; //Province.name

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public MatchState getState() {
        return state;
    }

    public void setState(MatchState state) {
        this.state = state;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchBeanResponse that = (MatchBeanResponse) o;
        return id.equals(that.id) &&
                Objects.equals(users, that.users) &&
                date.equals(that.date) &&
                state == that.state &&
                map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, date, state, map);
    }

    @Override
    public String toString() {
        return "MatchToListBean{" +
                "id=" + id +
                ", users=" + users +
                ", date=" + date +
                ", state=" + state +
                ", map='" + map + '\'' +
                '}';
    }
}
