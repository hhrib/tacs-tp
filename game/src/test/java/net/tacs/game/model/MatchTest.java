package net.tacs.game.model;

import net.tacs.game.services.UserService;
import net.tacs.game.services.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private final User user1 = new User("pepe");
    private final User user2 = new User("paula");
    @Autowired
    private UserService userService;

    @Test
    void Create_A_MatchAssertion()
    {
        long[] users = {1,2};
        Match newMatch = new Match("Buenos Aires", 10, users, userService);

        List<User> expectedUsersList = new ArrayList<User>();
        expectedUsersList.add(user1);
        expectedUsersList.add(user2);
        assertIterableEquals(expectedUsersList , newMatch.getUsers());
    }
}