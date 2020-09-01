package net.tacs.game.model;

import net.tacs.game.model.enums.MatchStatus;

import java.util.List;
import java.util.Objects;

public class Match {

    private Integer id;
    private List<User> users;
    private MatchStatus status;
    private Province map;
    private User winner;

    //TODO ¿Fecha en ISO8601? YYYYMMDDThhmmssZ
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Province getMap() {
        return map;
    }

    public void setMap(Province map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", users=" + users +
                ", status=" + status +
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
                status == match.status &&
                Objects.equals(map, match.map) &&
                Objects.equals(winner, match.winner) &&
                Objects.equals(date, match.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, status, map, winner, date);
    }
}
