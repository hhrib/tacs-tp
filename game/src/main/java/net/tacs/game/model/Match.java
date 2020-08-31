package net.tacs.game.model;

import java.util.List;
import java.util.Objects;

public class Match {

    private List<User> users;
    private MatchStatus status;
    private Province map;

    //TODO ¿Fecha en ISO8601? YYYYMMDDThhmmssZ
    private String date;

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
                "users=" + users +
                ", status=" + status +
                ", map=" + map +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return users.equals(match.users) &&
                status == match.status &&
                map.equals(match.map) &&
                Objects.equals(date, match.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, status, map, date);
    }
}
