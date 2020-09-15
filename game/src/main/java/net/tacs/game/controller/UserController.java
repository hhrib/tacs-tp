package net.tacs.game.controller;

import net.tacs.game.model.User;
import net.tacs.game.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(){
    }

    //TODO Mock devolviendo lista con un usuario. Versión preliminar.
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return Arrays.asList(new User("testUser"));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = this.userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users/{userName}")
    public ResponseEntity<User> getUserById(@PathVariable("userName") String userName) {
        User user =  this.userService.getUserByUserName(userName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}