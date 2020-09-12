package net.tacs.game.service;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.model.bean.CreateMatchBean;
import net.tacs.game.services.MatchService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MatchServiceTest {

    @Autowired
    private MatchService matchService;

    private final User user1 = new User("pepe");
    private final User user2 = new User("paula");

    @Test
    void createMatchOKTest() throws MatchException {

        CreateMatchBean bean = new CreateMatchBean();
        bean.setMunicipalitiesQty(3);
        bean.setProvinceId(1L);
        bean.setUserIds(Arrays.asList(1L,2L));
//        Match match = matchService.createMatch(bean);

        //La respuesta de matchService.createMatch por ahora está mockeada
//        assertEquals("Juan" , match.getMap().getMunicipalities().get(0).getOwner().getUsername());
    }
}