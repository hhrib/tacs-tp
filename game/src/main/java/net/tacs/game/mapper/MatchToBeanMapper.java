package net.tacs.game.mapper;

import net.tacs.game.model.Match;
import net.tacs.game.model.bean.MatchBeanResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MatchToBeanMapper {

    public static MatchBeanResponse mapMatch(Match matchToMap) {
        MatchBeanResponse bean = new MatchBeanResponse();
        bean.setId(matchToMap.getId());
        bean.setDate(matchToMap.getDate());
        bean.setMap(matchToMap.getMap().getName());
        bean.setState(matchToMap.getState());
        bean.setUsers(matchToMap.getUsers());

        return bean;
    }

    public static List<MatchBeanResponse> mapMatches(List<Match> matchesToMap) {
        return matchesToMap.stream().map(match -> mapMatch(match)).collect(Collectors.toList());
    }
}
