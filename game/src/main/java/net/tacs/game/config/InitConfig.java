package net.tacs.game.config;

import net.tacs.game.GameApplication;
import net.tacs.game.mapper.AuthUserToUserMapper;
import net.tacs.game.model.Match;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import net.tacs.game.model.enums.MatchState;
//import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.model.opentopodata.auth.AuthUserResponse;
import net.tacs.game.repositories.ProvinceRepository;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.services.ProvinceService;
import net.tacs.game.services.SecurityProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Profile("!test")
@Configuration
public class InitConfig {

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityProviderService securityProviderService;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            List<Province> provincesTemp = provinceService.findAll();

            for (Province aProvince : provincesTemp) {
                provinceRepository.add(aProvince);
            }

            String authToken = securityProviderService.getToken();
            List<AuthUserResponse> authUsers = securityProviderService.getUsers(authToken);
            userRepository.setUsers(AuthUserToUserMapper.mapUsers(authUsers));
            GameApplication.setToken(authToken);
        };
    }
}
