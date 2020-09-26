package net.tacs.game.config;

import net.tacs.game.GameApplication;
import net.tacs.game.mapper.AuthUserToUserMapper;
import net.tacs.game.model.Match;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import net.tacs.game.model.enums.MatchState;
import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.model.opentopodata.auth.AuthUserResponse;
import net.tacs.game.services.ProvinceService;
import net.tacs.game.services.SecurityProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
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
                addProvince(aProvince);
            }

//            String authToken = securityProviderService.getToken();
//            List<AuthUserResponse> authUsers = securityProviderService.getUsers(authToken);
//            GameApplication.setUsers(AuthUserToUserMapper.mapUsers(authUsers));
//            GameApplication.setToken(authToken);
            Match match = new Match();
            Province prov = new Province();
            
            User userTemp = new User();
            userTemp.setUsername("ale");
            userTemp.setId("132a");
            
            Municipality muniTemp = new Municipality();
            muniTemp.setId(1111);
            muniTemp.setOwner(userTemp);
            muniTemp.setGauchosQty(11);
            muniTemp.setState(MunicipalityState.PRODUCTION);
            prov.setMunicipalities(Arrays.asList(muniTemp));

            match.setId(1234L);
            match.setMap(prov);
            match.setState(MatchState.CREATED);

            GameApplication.addMatch(match);
            

        };
    }
}
