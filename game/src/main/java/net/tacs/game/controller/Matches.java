package net.tacs.game.controller;

import net.tacs.game.model.Match;
import net.tacs.game.model.enums.MatchStatus;
import net.tacs.game.model.Province;
import net.tacs.game.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class Matches {

    private static final Logger LOGGER = LoggerFactory.getLogger(Matches.class);

    @GetMapping("/matches")
    public List<Match> getAllMatches() {
        List<Match> matches = new ArrayList<>();
        matches.add(dummyMatch());

        return matches;
    }

    @PostMapping(value = "/matches")
    public ResponseEntity<Match> createMatch(@RequestBody Match newMatch) {
        //TODO Validar que venga aunque sea un usuario y que venga el mapa.
        newMatch.setDate(DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()));
        newMatch.setStatus(MatchStatus.CREATED);
        LOGGER.info(newMatch.toString());
        return new ResponseEntity<Match>(newMatch, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/matches/{id}")
    public ResponseEntity<Match> modifyMatchState(@RequestBody(required = true) MatchStatus statusRequest,
                                                    @PathParam(value = "id") String id){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    private Match dummyMatch() {
        Match match = new Match();
        match.setDate("20200901T220000Z");
        match.setStatus(MatchStatus.IN_PROGRESS);
        match.setUsers(Arrays.asList(new User("testUser"), new User("testUserRival")));
        match.setMap(new Province("Córdoba"));
        return match;
    }

}
