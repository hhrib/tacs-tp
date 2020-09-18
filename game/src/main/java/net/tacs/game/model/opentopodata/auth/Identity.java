package net.tacs.game.model.opentopodata.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Identity {

    private String provider;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Double expiresIn;

    @JsonProperty("user_id")
    private String userId;

    private String connection;

    private Boolean isSocial;

    public String getProvider() {
        return provider;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Double getExpiresIn() {
        return expiresIn;
    }

    public String getUserId() {
        return userId;
    }

    public String getConnection() {
        return connection;
    }

    public Boolean getIsSocial() {
        return isSocial;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiresIn(Double expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public void setIsSocial(Boolean isSocial) {
        this.isSocial = isSocial;
    }
}
