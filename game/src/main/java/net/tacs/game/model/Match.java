package net.tacs.game.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import net.tacs.game.model.enums.MatchState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Match {

    private Long id;

    private List<User> users;

    private MatchState state;

    private Province map;

    private User winner;

    private MatchConfiguration config;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    public LocalDateTime date;

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

    public MatchState getState() {
        return state;
    }

    //User story 2.c
    public void setState(MatchState state) {
        this.state = state;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Province getMap() {
        return map;
    }

    public void setMap(Province map) {
        this.map = map;
    }

    public MatchConfiguration getConfig() {
        return config;
    }

    public void setConfig(MatchConfiguration config) {
        this.config = config;
    }

    public Match(){
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", users=" + users +
                ", state=" + state +
                ", map=" + map +
                ", winner=" + winner +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id) &&
                Objects.equals(users, match.users) &&
                state == match.state &&
                Objects.equals(map, match.map) &&
                Objects.equals(winner, match.winner) &&
                Objects.equals(date, match.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, state, map, winner, date);
    }
}
