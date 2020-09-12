package net.tacs.game.services;

import java.util.List;

import net.tacs.game.model.Centroide;
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
	public Municipality[] findMunicipios(int provinceId, Integer qty) ;
	/**
	 * 
	 * @param location
	 * @return Elvation for determined latitude and longitude
	 */
	public Double getElevation(Centroide location);
}
