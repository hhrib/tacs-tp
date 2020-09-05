package net.tacs.game.controller;

import net.tacs.game.exceptions.MatchIsEmptyException;
import net.tacs.game.model.Match;
import net.tacs.game.model.User;
import net.tacs.game.repositories.MatchRepository;
import net.tacs.game.repositories.UserRepository;
import net.tacs.game.services.MatchService;
import net.tacs.game.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class Matches {

    private static final Logger LOGGER = LoggerFactory.getLogger(Matches.class);

    @Autowired
    private MatchService matchService;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    //User story 2.b
    @GetMapping("/matches")
    public Iterable<Match> getAllMatches() {
        
        //TODO SOLO PARA PROBAR
        User userPepe = new User("pepe");
        userRepository.save(userPepe);
        User userPaula = new User("paula");
        userRepository.save(userPaula);
        matchRepository.save(new Match("Buenos Aires", 10, new long[]{1,2}, userService));
        
        return matchService.findAll();
    }

    //User story 2.a
    @PostMapping(value = "/matches")
    public ResponseEntity<Match> createMatch(@RequestBody Match newMatch) {
        //TODO Validar que venga el mapa.
        if(newMatch.getUsers().isEmpty())
            throw new MatchIsEmptyException("The Match Created has no Players");

        newMatch.setDate(DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()));
        LOGGER.info(newMatch.toString());
        return new ResponseEntity<Match>(newMatch, HttpStatus.CREATED);
    }

}
