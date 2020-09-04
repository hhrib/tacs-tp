package net.tacs.game.services;

import net.tacs.game.model.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.tacs.game.repositories.MatchRepository;

@Service("matchService")
@Transactional
public class MatchServiceImplementation implements MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Override
    public Iterable<Match> findAll() {
        return matchRepository.findAll();
    }

}
