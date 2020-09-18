package net.tacs.game.config;

import net.tacs.game.GameApplication;
import net.tacs.game.mapper.AuthUserToUserMapper;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import net.tacs.game.model.opentopodata.auth.AuthUserResponse;
import net.tacs.game.services.ProvinceService;
import net.tacs.game.services.SecurityProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

import static net.tacs.game.GameApplication.addProvince;
import static net.tacs.game.GameApplication.addUser;

@Profile("!test")
@Configuration
public class InitConfig {

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private SecurityProviderService securityProviderService;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            List<Province> provincesTemp = provinceService.findAll();

            for (Province aProvince : provincesTemp) {
                aProvince.setMunicipalities(provinceService.findMunicipios((int)aProvince.getId(), null));

                addProvince(aProvince);
            }

            String authToken = securityProviderService.getToken();
            List<AuthUserResponse> authUsers = securityProviderService.getUsers(authToken);
            GameApplication.setUsers(AuthUserToUserMapper.mapUsers(authUsers));

//            User user1 = new User("Juan");
//            User user2 = new User("Ale");
//            User user3 = new User("Emi");
//            User user4 = new User("Hernan");
//            User user5 = new User("Fer");
//            user1.setId(1L);
//            user2.setId(2L);
//            user3.setId(3L);
//            user4.setId(4L);
//            user5.setId(5L);
//            addUser(user1);
//            addUser(user2);
//            addUser(user3);
//            addUser(user4);
//            addUser(user5);
        };
    }
}
