package net.tacs.game.services;

import java.util.List;

import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;

public interface ProvinceService {
	public List<Province> findAll();
	/**
	 * 
	 * @param provinceId
	 * @param qty
	 * @return all provinces for a determined province
	 */
	public List<Municipality> findMunicipios(int provinceId, Integer qty) ;
}
