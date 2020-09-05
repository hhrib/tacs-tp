package net.tacs.game.services;

import net.tacs.game.model.User;

import java.util.Optional;

public interface UserService {
    public Optional<User> findById(long id);
}
