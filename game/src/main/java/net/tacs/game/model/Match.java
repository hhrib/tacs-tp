package net.tacs.game.model;

import net.tacs.game.model.enums.MatchState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Match {

    private Long id;

    // @OneToMany(cascade = {CascadeType.ALL})
    private List<User> users;
    private MatchState state;
   // @OneToOne
    private Province map;

    private LocalDateTime date;

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

    public Match(){
    }
    //User story 2.a
//    TODO? agregar configuraciones al crear partida
//    @Autowired
//    public Match(String provinceName, Integer municipalitiesQty, long[] player_ids, UserService userService)
//    {
//        this.userService = userService;
//        this.users = new ArrayList<User>();
//        map = new Province(provinceName, municipalitiesQty);
//
//        search_users(player_ids);
//
//        state = MatchState.CREATED;
//    }

//    /**
//     * @method search_users
//     * @param player_ids
//     * Busca en la aplicacion los usuarios que corresponden a la partida y los agrega a la lista
//     */
//    private void search_users(long[] player_ids)
//    {
//        for (long player_id : player_ids) {
//            User user = userService.findById(player_id).orElseThrow(() -> new UserNotFoundException(player_id));
//            users.add(user);
//        }
//    }


    @Override
    public String toString() {
        return "Match{" +
                "users=" + users +
                ", status=" + state +
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
                state == match.state &&
                map.equals(match.map) &&
                Objects.equals(date, match.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, state, map, date);
    }
}
