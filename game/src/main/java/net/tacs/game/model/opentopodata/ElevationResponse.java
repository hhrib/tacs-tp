package net.tacs.game.model.opentopodata;

public class ElevationResponse {
	private String status;
	private ElevationBean[] results;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ElevationBean[] getResults() {
		return results;
	}

	public void setResults(ElevationBean[] results) {
		this.results = results;
	}

}
