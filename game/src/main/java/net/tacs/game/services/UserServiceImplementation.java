package net.tacs.game.services;

import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("userService")
@Transactional
public class UserServiceImplementation implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
}
