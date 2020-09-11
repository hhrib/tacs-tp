package net.tacs.game.services.impl;

import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("userService")
//@Transactional
public class UserServiceImpl implements UserService {

//    @Autowired
//    private UserRepository userRepository;

    //TODO Ir a buscar al mapa que persiste en memoria para primeras entregas.
    @Override
    public List<User> findAll() {
        return null;
    }

    //TODO Ir a buscar al mapa que persiste en memoria para primeras entregas.
    @Override
    public User getUserById(Long id) {
        return null;
    }

    //TODO Ir a buscar al mapa que persiste en memoria para primeras entregas.
    @Override
    public User getUserByUserName(String userName) {
        return null;
    }
}
