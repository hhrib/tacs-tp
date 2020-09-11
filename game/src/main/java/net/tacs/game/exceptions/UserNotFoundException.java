package net.tacs.game.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long player_id) {
        super("User with id: " + player_id + "not found");
    }
}
