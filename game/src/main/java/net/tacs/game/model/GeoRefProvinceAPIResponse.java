package net.tacs.game.model;

public class GeoRefProvinceAPIResponse {
	private Province[] provincias;
	private int cantidad;
	private int total;
	private int inicio;
	
	public GeoRefProvinceAPIResponse() {
		super();
	}
	
	public Province[] getProvincias() {
		return provincias;
	}

	public void setProvincias(Province[] provincias) {
		this.provincias = provincias;
	}

	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getInicio() {
		return inicio;
	}
	public void setInicio(int inicio) {
		this.inicio = inicio;
	}
	
}
