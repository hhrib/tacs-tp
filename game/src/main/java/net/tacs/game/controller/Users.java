package net.tacs.game.controller;

import net.tacs.game.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class Users {

    public Users(){
    }

    //TODO Mock devolviendo lista con un usuario. Versión preliminar.
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return Arrays.asList(new User("testUser"));
    }


}