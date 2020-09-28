package net.tacs.game.mapper;

import net.tacs.game.model.Match;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.dto.MatchDTOResponse;
import net.tacs.game.model.dto.MuniStatisticsDTOResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MuniToStatsDTOMapper {

    public static MuniStatisticsDTOResponse mapMuni(Municipality muniToMap) {
        MuniStatisticsDTOResponse dto = new MuniStatisticsDTOResponse();
        dto.setMuniId(muniToMap.getId());
        dto.setGauchosQty(muniToMap.getGauchosQty());
        dto.setState(muniToMap.getState());

        return dto;
    }

    public static List<MuniStatisticsDTOResponse> mapMunis(List<Municipality> munisToMap) {
        return munisToMap.stream().map(muni -> mapMuni(muni)).collect(Collectors.toList());
    }

}
