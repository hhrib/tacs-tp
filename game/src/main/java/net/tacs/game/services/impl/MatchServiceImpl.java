package net.tacs.game.services.impl;

import net.tacs.game.controller.MatchController;
import net.tacs.game.exceptions.MatchException;
import net.tacs.game.model.*;
import net.tacs.game.model.bean.CreateMatchBean;
import net.tacs.game.model.enums.MatchState;
import net.tacs.game.model.enums.MunicipalityState;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.services.MatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import net.tacs.game.repositories.MatchRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static net.tacs.game.GameApplication.*;

@Service("matchService")
//@Transactional
public class MatchServiceImpl implements MatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchController.class);

//    @Autowired
//    private MatchRepository matchRepository;
//
//    @Autowired
//    private UserRepository userRepository;

    @Override
    public List<Match> findAll() {
        //SOLO PARA PROBAR
//        User userPepe = new User("pepe");
//        userRepository.save(userPepe);
//        User userPaula = new User("paula");
//        userRepository.save(userPaula);
//        matchRepository.save(new Match("Buenos Aires", 10, new long[]{1,2}, userService));
//        return matchRepository.findAll();
        //TODO Ir a buscar al mapa que persiste en memoria para primeras entregas.
        List<Match> matches = Arrays.asList(new Match());
        return matches;
    }

    @Override
    public Match createMatch(CreateMatchBean newMatchBean) throws MatchException {

        Match newMatch = validateAndGenerateMatch(newMatchBean);

        newMatch.setDate(LocalDateTime.now());
        newMatch.setState(MatchState.CREATED);
        LOGGER.info(newMatchBean.toString());
        //TODO Salvar partida en el mapa que persiste en memoria para primeras entregas.
        //Quizás no recibamos un Match entero, quizás recibamos el Bean de creación (dependiendo lo que elijamos)
        //en ese caso va a haber que hacer la lógica de buscar usuarios y provincia y luego crear el objeto match para
        //persistir

        return newMatch;
    }

    private Match validateAndGenerateMatch(CreateMatchBean newMatchBean) throws MatchException {
        List<ApiError> errors = new ArrayList<>();
        //TODO Algunas de estas validaciones se pueden hacer con annotations en los models. Revisar.
        if (newMatchBean.getUserIds() == null || newMatchBean.getUserIds().isEmpty()) {
            errors.add(new ApiError("MATCH_EMPTY_USERS", "Match Players not specified"));
        }
        if (newMatchBean.getUserIds().size() < 2) {
            errors.add(new ApiError("MATCH_INSUFFICIENT_NUMBER_USERS", "Match needs at least 2 players"));
        }
        if (newMatchBean.getProvinceId() == null) {
            errors.add(new ApiError("MATCH_EMPTY_MAP", "Match Map not specified"));
        }

        if (!errors.isEmpty()) {
            throw new MatchException(HttpStatus.BAD_REQUEST, errors);
        }

        //Buscar Provincia, y Users en el mapa que persiste en memoria para primeras entregas:
        //Si no se encuentra alguno, se agrega error de 404 NOT_FOUND

        Match newMatch = new Match();

        List<User> usersInMatch = new ArrayList<>();
        for(long aId : newMatchBean.getUserIds())
        {
            boolean bUserFound = false;

            for(User aUser : getUsers())
            {
                if(aUser.getId() == aId)
                {
                    bUserFound = true;
                    usersInMatch.add(aUser);
                }
            }

            if(!bUserFound)
            {
                errors.add(new ApiError("USER_NOT_FOUND", "Users not found"));
                return newMatch;
            }
        }

        newMatch.setUsers(usersInMatch);

        Province newProvince = new Province();

        //TODO Buscar en base de datos
        for (Province aProvince: getProvinces()) {
            if(aProvince.getId().equals(newMatchBean.getProvinceId()))
            {
                //Copia de Provincia
                newProvince.setName(aProvince.getName());

                if (newMatchBean.getMunicipalitiesQty() < aProvince.getMunicipalities().size()) {
                    errors.add(new ApiError("EXCEEDED_MUNICIPALITIES_LIMIT",
                            "Amount of municipalities selected exceeds amount of province's amount of municipalities"));

                    return newMatch;
                }

                Random random = new Random();
                List<Municipality> tempMunicipalities = aProvince.getMunicipalities();
                List<Integer> selectedIndexes = new ArrayList<>();

                //Crear Municipalidades
                for(int i = 1; i <= newMatchBean.getMunicipalitiesQty(); i++)
                {
                    Municipality muni = new Municipality();

                    int selectedMuniIndex = random.nextInt(tempMunicipalities.size());
                    while(selectedIndexes.contains(selectedMuniIndex))
                    {
                        selectedMuniIndex = random.nextInt(tempMunicipalities.size());
                    }

                    muni.setName(tempMunicipalities.get(selectedMuniIndex).getName());
                    selectedIndexes.add(selectedMuniIndex);

                    //TODO o buscar de la api o de la cache
                    muni.setElevation(tempMunicipalities.get(selectedMuniIndex).getElevation());

                    //TODO Gauchos son los mismos para todos?
                    muni.setGauchosQty(tempMunicipalities.get(selectedMuniIndex).getGauchosQty());

                    //TODO muni.setProvince(province); <--- la municipalidad necesita saber la provincia a la que pertenece?

                    //TODO Por defecto se pone en defensa?
                    muni.setState(MunicipalityState.DEFENSE);

                    newProvince.addMunicipality(muni);
                }

                newMatch.setMap(newProvince);

                //asignar municipalidades a usuarios
                assignMunicipalities(newMatch.getMap().getMunicipalities(), newMatch.getUsers());
            }

            return newMatch;
        }

        errors.add(new ApiError("PROVINCE_NOT_FOUND",
                "Province Id does not exist"));

        return newMatch;
    }

    private void assignMunicipalities(List<Municipality> municipalities, List<User> users)
    {
        int usersIndex = 0;
        for(Municipality aMuni : municipalities)
        {
            aMuni.setOwner(users.get(usersIndex));

            usersIndex++;

            if(usersIndex == users.size())
            {
                usersIndex = 0;
            }
        }
    }
}
