package net.tacs.game.mapper;

import net.tacs.game.model.User;
import net.tacs.game.model.dto.UserDTOResponse;

import java.util.List;
import java.util.stream.Collectors;

public class UserToDTOMapper {

    public static UserDTOResponse userBeanResponse(User userToMap) {
        UserDTOResponse bean = new UserDTOResponse();
        bean.setId(userToMap.getId());
        bean.setName(userToMap.getUsername());

        return bean;
    }

    public static List<UserDTOResponse> mapUsers(List<User> usersToMap) {
        return usersToMap.stream().map(user -> userBeanResponse(user)).collect(Collectors.toList());
    }
}
