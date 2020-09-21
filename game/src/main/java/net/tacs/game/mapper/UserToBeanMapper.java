package net.tacs.game.mapper;

import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import net.tacs.game.model.bean.ProvinceBeanResponse;
import net.tacs.game.model.bean.UserBeanResponse;

import java.util.List;
import java.util.stream.Collectors;

public class UserToBeanMapper {

    public static UserBeanResponse userBeanResponse(User userToMap) {
        UserBeanResponse bean = new UserBeanResponse();
        bean.setId(userToMap.getId());
        bean.setName(userToMap.getUsername());

        return bean;
    }

    public static List<UserBeanResponse> mapUsers(List<User> usersToMap) {
        return usersToMap.stream().map(user -> userBeanResponse(user)).collect(Collectors.toList());
    }
}
