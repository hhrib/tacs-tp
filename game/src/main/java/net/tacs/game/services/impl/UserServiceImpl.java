package net.tacs.game.services.impl;

import net.tacs.game.GameApplication;
import net.tacs.game.mapper.AuthUserToUserMapper;
import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.services.SecurityProviderService;
import net.tacs.game.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static net.tacs.game.GameApplication.getUsers;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SecurityProviderService securityProviderService;

    @Override
    public List<User> findAll() throws Exception {
        //Se va temporalmente a api de Auth0 hasta que resolvamos el Webhook que nos avise del registro de un nuevo usuario
        return AuthUserToUserMapper.mapUsers(securityProviderService.getUsers(GameApplication.getToken()));
//        return getUsers();
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
