package net.tacs.game.mapper;

import net.tacs.game.model.Match;
import net.tacs.game.model.dto.MatchDTOResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MatchToDTOMapper {

    public static MatchDTOResponse mapMatch(Match matchToMap) {
        MatchDTOResponse bean = new MatchDTOResponse();
        bean.setId(matchToMap.getId());
        bean.setDate(matchToMap.getDate());
        bean.setMap(matchToMap.getMap().getNombre());
        bean.setState(matchToMap.getState());
        bean.setUsers(matchToMap.getUsers());

        return bean;
    }

    public static List<MatchDTOResponse> mapMatches(List<Match> matchesToMap) {
        return matchesToMap.stream().map(match -> mapMatch(match)).collect(Collectors.toList());
    }
}
