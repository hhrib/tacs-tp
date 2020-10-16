package net.tacs.game.controller;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.mapper.UserToDTOMapper;
import net.tacs.game.model.User;
import net.tacs.game.model.UserStats;
import net.tacs.game.model.dto.Auth0NewUserDTO;
import net.tacs.game.model.dto.Scoreboard;
import net.tacs.game.model.dto.UserDTOResponse;
import net.tacs.game.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    @Autowired
    private UserService userService;

    public UserController(){
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTOResponse>> getAllUsers() throws Exception {
        List<User> users = userService.findAll();
        LOGGER.info("users qty: {}", users.size());
        return new ResponseEntity<>(UserToDTOMapper.mapUsers(users), HttpStatus.OK);
    }

    @GetMapping("/users/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        User user = this.userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users/username/{userName}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("userName") String userName) {
        User user =  this.userService.getUserByUserName(userName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users/id/{id}/stats")
    public ResponseEntity<UserStats> getUserStatsById(@PathVariable("id") String id)
    {
        UserStats userStats = userService.getUserStatistics(id);
        return new ResponseEntity<>(userStats, HttpStatus.OK);
    }

    @GetMapping("/users/username/{username}/stats")
    public ResponseEntity<UserStats> getUserStatsByUsername(@PathVariable("username") String username)
    {
        User user = this.userService.getUserByUserName(username);
        UserStats userStats = userService.getUserStatistics(user.getId());
        return new ResponseEntity<>(userStats, HttpStatus.OK);
    }

    @GetMapping("/users/scoreboard")
    public ResponseEntity<Scoreboard> getScoreboard()
    {
        Scoreboard scoreboard = this.userService.getScoreboard();
        return new ResponseEntity<>(scoreboard, HttpStatus.OK);
    }

    @PostMapping("/users/auth0")
    public ResponseEntity<User> addUserByHook(@RequestBody Auth0NewUserDTO userBean) throws MatchException {
        LOGGER.info("Nuevo usuario desde Auth0: " + userBean);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}