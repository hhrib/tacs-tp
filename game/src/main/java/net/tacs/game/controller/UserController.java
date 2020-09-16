package net.tacs.game.controller;

import net.tacs.game.mapper.ProvinceToBeanMapper;
import net.tacs.game.mapper.UserToBeanMapper;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import net.tacs.game.model.bean.ProvinceBeanResponse;
import net.tacs.game.model.bean.UserBeanResponse;
import net.tacs.game.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static net.tacs.game.GameApplication.getUsers;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    @Autowired
    private UserService userService;

    public UserController(){
    }

    //TODO Mock devolviendo lista con un usuario. Versión preliminar.
    @GetMapping("/users")
    public ResponseEntity<List<UserBeanResponse>> getAllUsers() {
        List<User> users = userService.findAll();
        LOGGER.info("users qty: {}", users.size());
        return new ResponseEntity<>(UserToBeanMapper.mapUsers(users), HttpStatus.OK);
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