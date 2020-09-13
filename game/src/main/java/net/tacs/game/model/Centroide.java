package net.tacs.game.model;

public class Centroide {
	private String lat;
	private String lon;

	public Centroide() {
		super();
	}

	public Centroide(String lat, String lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	@Override
	public String toString() {
		return getLat().concat(",").concat(getLon());
	}

}
