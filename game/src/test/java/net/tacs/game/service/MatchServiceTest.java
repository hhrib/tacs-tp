package net.tacs.game.service;

import static net.tacs.game.GameApplication.addMunicipality;
import static net.tacs.game.GameApplication.addProvince;
import static net.tacs.game.GameApplication.addUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.Match;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import net.tacs.game.model.bean.CreateMatchBean;
import net.tacs.game.services.MatchService;

@SpringBootTest
public class MatchServiceTest {

    @Autowired
    private MatchService matchService;

    private final User user1 = new User("Pepe");
    private final User user2 = new User("Paula");
    private final Province buenosAires = new Province("Buenos Aires");
    private final Municipality lanus = new Municipality("Lanus");
    private final Municipality avellaneda = new Municipality("Avellaneda");
    private final Municipality quilmes = new Municipality("Quilmes");
    private final Municipality tigre = new Municipality("Tigre");
    private final Municipality lomas = new Municipality("Lomas de Zamora");
    private final Municipality matanza = new Municipality("La Matanza");

    @Test
    void createMatchOKTest() throws MatchException {

        CreateMatchBean bean = new CreateMatchBean();
        bean.setMunicipalitiesQty(6);
        bean.setProvinceId(1L);
        bean.setUserIds(Arrays.asList(99999998L,99999999L));

        addProvince(buenosAires);
        addMunicipality(lanus);
        addMunicipality(avellaneda);
        addMunicipality(quilmes);
        addMunicipality(tigre);
        addMunicipality(lomas);
        addMunicipality(matanza);
        addUser(user1);
        addUser(user2);

        List<Municipality> municipalityList = new ArrayList<>();
        municipalityList.add(lanus);
        municipalityList.add(quilmes);
        municipalityList.add(avellaneda);
        municipalityList.add(tigre);
        municipalityList.add(lomas);
        municipalityList.add(matanza);

        buenosAires.setId(1);
        buenosAires.setMunicipalities(municipalityList);

        user1.setId(99999998L);
        user2.setId(99999999L);

        lanus.setElevation(3D);
        avellaneda.setElevation(3D);
        quilmes.setElevation(3D);
        tigre.setElevation(3D);
        lomas.setElevation(3D);
        matanza.setElevation(3D);

        lanus.setGauchosQty(300);
        avellaneda.setGauchosQty(300);
        quilmes.setGauchosQty(300);
        tigre.setGauchosQty(300);
        lomas.setGauchosQty(300);
        matanza.setGauchosQty(300);

        Match match = matchService.createMatch(bean);

        //chequea que se hayan agregado correctamente los usuarios, la provincia y los municipios
        assertEquals("Pepe" , match.getUsers().get(0).getUsername());
        assertEquals("Paula" , match.getUsers().get(1).getUsername());
        assertEquals("Buenos Aires", match.getMap().getNombre());
        assertTrue(match.getMap().getMunicipalities().contains(lanus));
        assertTrue(match.getMap().getMunicipalities().contains(avellaneda));
        assertTrue(match.getMap().getMunicipalities().contains(quilmes));
        assertTrue(match.getMap().getMunicipalities().contains(tigre));
        assertTrue(match.getMap().getMunicipalities().contains(lomas));
        assertTrue(match.getMap().getMunicipalities().contains(matanza));

        //chequea que cada usuario tenga la misma cantidad de municipios
        int user1Munis = 0;
        int user2Munis = 0;

        for(Municipality aMuni : match.getMap().getMunicipalities())
        {
            switch (aMuni.getOwner().getId().intValue())
            {
                case 99999998:
                {
                    user1Munis++;
                    break;
                }
                case 99999999:
                {
                    user2Munis++;
                    break;
                }
            }

        }

        assertEquals(3, user1Munis);
        assertEquals(3, user2Munis);
    }
}