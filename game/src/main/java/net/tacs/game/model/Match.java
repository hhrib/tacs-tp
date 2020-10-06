package net.tacs.game.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import net.tacs.game.model.dto.AttackMuniDTO;
import net.tacs.game.model.dto.AttackResultDTO;
import net.tacs.game.model.enums.MatchState;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Match {

    private static long idCounter = 0;
    private Long id;

    private List<User> users;

    private MatchState state;

    private Province map;

    private User turnPlayer;

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

    public User getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(User turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public Match(){
        id = idCounter++;
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

    public boolean userIsInMatch(String userId) {
        for(User aUser : getUsers())
        {
            if(aUser.getId().equals(userId))
                return true;
        }

        return false;
    }

    public void checkVictory(User player) {
        int playerMunis = player.municipalitiesOwning(this.getMap().getMunicipalities());

        if(playerMunis == 0)
        {
            this.getConfig().removePlayer(player);
        }

        if(getConfig().getPlayersTurns().size() == 1) //solo quedo un jugador
        {
            this.winner = this.getConfig().getPlayersTurns().get(0);
            this.setState(MatchState.FINISHED);
        }
    }

    public boolean playerCanAttack(User player) {
        return player.equals(turnPlayer);
    }
}
