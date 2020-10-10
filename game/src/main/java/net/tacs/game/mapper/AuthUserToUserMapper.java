package net.tacs.game.mapper;

import net.tacs.game.model.User;
import net.tacs.game.model.opentopodata.auth.AuthUserResponse;

import java.util.List;
import java.util.stream.Collectors;

public class AuthUserToUserMapper {

    public static User mapUser(AuthUserResponse authUserToMap) {
        User user = new User();
        user.setId(authUserToMap.getUserId());
        user.setUsername(authUserToMap.getNickname());

        return user;
    }

    public static List<User> mapUsers(List<AuthUserResponse> authUsersToMap) {
        return authUsersToMap.stream().map(authUser -> mapUser(authUser)).collect(Collectors.toList());
    }

}
