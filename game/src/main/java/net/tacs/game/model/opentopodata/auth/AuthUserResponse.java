package net.tacs.game.model.opentopodata.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AuthUserResponse {

    @JsonProperty("created_at")
    private String createdAt;

    private String email;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("given_name")
    private String givenName;

    private List<Identity> identities = new ArrayList<Identity>();

    private String locale;

    private String name;

    private String nickname;

    private String picture;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("last_login")
    private String lastLogin;

    @JsonProperty("last_ip")
    private String lastIp;

    @JsonProperty("logins_count")
    private Integer loginsCount;


    public String getCreatedAt() {
        return createdAt;
    }

    public String getEmail() {
        return email;
    }

    public boolean getEmailVerified() {
        return emailVerified;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public List<Identity> getIdentities() {
        return identities;
    }

    public String getLocale() {
        return locale;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPicture() {
        return picture;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public String getLastIp() {
        return lastIp;
    }

    public float getLoginsCount() {
        return loginsCount;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setGiveName(String giveName) {
        this.givenName = giveName;
    }

    public void setIdentities(List<Identity> identities) {
        this.identities = identities;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public void setLoginsCount(Integer loginsCount) {
        this.loginsCount = loginsCount;
    }

}
