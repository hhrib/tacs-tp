package net.tacs.game.services;

import net.tacs.game.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> findAll();

    public User getUserById(Long id);

    public User getUserByUserName(String userName);

}
