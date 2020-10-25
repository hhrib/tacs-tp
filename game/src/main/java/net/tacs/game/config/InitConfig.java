package net.tacs.game.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import net.tacs.game.GameApplication;
import net.tacs.game.mapper.AuthUserToUserMapper;
import net.tacs.game.model.Province;
//import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.model.opentopodata.auth.AuthUserResponse;
import net.tacs.game.repositories.ProvinceRepository;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.services.ProvinceService;
import net.tacs.game.services.SecurityProviderService;

@Profile("!test")
@Configuration
public class InitConfig {

	//TODO entiendo que no haría falta esta parte
    @Autowired
    private ProvinceService provinceService;
//
    @Autowired
    private ProvinceRepository provinceRepository;
//
    @Autowired
    private UserRepository userRepository;
//
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
            userRepository.saveAll(AuthUserToUserMapper.mapUsers(authUsers));
            GameApplication.setToken(authToken);
        };
    }
}
