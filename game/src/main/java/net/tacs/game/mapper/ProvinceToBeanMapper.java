package net.tacs.game.mapper;

import net.tacs.game.model.Province;
import net.tacs.game.model.bean.ProvinceBeanResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ProvinceToBeanMapper {

    public static ProvinceBeanResponse mapProvince(Province provinceToMap) {
        ProvinceBeanResponse bean = new ProvinceBeanResponse();
        bean.setId(provinceToMap.getId());
        bean.setName(provinceToMap.getNombre());

        return bean;
    }

    public static List<ProvinceBeanResponse> mapProvinces(List<Province> provincesToMap) {
        return provincesToMap.stream().map(province -> mapProvince(province)).collect(Collectors.toList());
    }

}
