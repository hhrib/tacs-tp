package net.tacs.game.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "match")
public class Match {

    private @Id @GeneratedValue int id;

    //TODO POR AHORA LO DEJO EN TRANSIENT
    @Transient
    private List<User> users;
    private MatchStatus status;

    //TODO POR AHORA LO DEJO EN TRANSIENT
    @Transient
    //TODO provincia por ahora no tiene metodos
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

    //User story 2.c
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

    public Match(){
    }
    //User story 2.a
    //TODO? agregar configuraciones al crear partida
    public Match(String province, int cant_municipalities, int[] player_ids)
    {
        map = new Province(province, cant_municipalities);

        search_users(player_ids);

        status = MatchStatus.CREATED;
    }

    /**
     * @method search_users
     * @param player_ids
     * Busca en la aplicacion los usuarios que corresponden a la partida y los agrega a la lista
     */
    private void search_users(int[] player_ids)
    {
        for(int i = 0; i < player_ids.length; i++)
        {
            //users.add(User user = repository.findById(player_ids[i])
            //                .orElseThrow(() -> new UserNotFoundException(player_ids[i]));)
        }
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
