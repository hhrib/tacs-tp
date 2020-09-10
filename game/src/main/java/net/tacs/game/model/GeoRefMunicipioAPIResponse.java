package net.tacs.game.model;

public class GeoRefMunicipioAPIResponse {
	private Municipality[] municipios;
	private int cantidad;
	private int total;
	private int inicio;
	
	public GeoRefMunicipioAPIResponse() {
		super();
	}
	
	public Municipality[] getMunicipios() {
		return municipios;
	}

	public void setMunicipios(Municipality[] municipios) {
		this.municipios = municipios;
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
