package net.tacs.game.mapper;

import net.tacs.game.model.Province;
import net.tacs.game.model.dto.ProvinceDTOResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ProvinceToDTOMapper {

    public static ProvinceDTOResponse mapProvince(Province provinceToMap) {
        ProvinceDTOResponse bean = new ProvinceDTOResponse();
        bean.setId(provinceToMap.getId());
        bean.setName(provinceToMap.getNombre());

        return bean;
    }

    public static List<ProvinceDTOResponse> mapProvinces(List<Province> provincesToMap) {
        return provincesToMap.stream().map(province -> mapProvince(province)).sorted(ProvinceDTOResponse::compareTo).collect(Collectors.toList());
    }

}
