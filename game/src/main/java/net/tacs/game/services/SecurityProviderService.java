package net.tacs.game.services;

import net.tacs.game.model.opentopodata.auth.AuthUserResponse;

import java.util.List;

public interface SecurityProviderService {

    public String getToken() throws Exception;

    public List<AuthUserResponse> getUsers(String authToken) throws Exception;

}
