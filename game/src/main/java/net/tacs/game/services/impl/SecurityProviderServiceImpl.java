package net.tacs.game.services.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tacs.game.model.opentopodata.auth.AuthTokenResponse;
import net.tacs.game.model.opentopodata.auth.AuthUserResponse;
import net.tacs.game.services.SecurityProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SecurityProviderServiceImpl implements SecurityProviderService {

    @Value("${auth0.grant.type}")
    private String grantType;

    @Value("${auth0.client.id}")
    private String clientId;

    @Value("${auth0.client.scrt}")
    private String scrt;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.oauth.url}")
    private String oauthUrl;

    @Value("${auth0.endpoint.users}")
    private String endpointUsers;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", grantType);
        map.add("client_id", clientId);
        map.add("client_secret",scrt);
        map.add("audience", audience);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(oauthUrl, request, String.class);

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            handleErrorResponse(response);
        }

        AuthTokenResponse tokenResponse = objectMapper.readValue(response.getBody(), AuthTokenResponse.class);

        return tokenResponse.getAccessToken();
    }



    @Override
    public List<AuthUserResponse> getUsers(String authToken) throws Exception {
        HttpEntity request = generateRequestEntity(authToken);

        ResponseEntity<String> response = restTemplate.exchange(
                audience.concat(endpointUsers),
                HttpMethod.GET,
                request,
                String.class);

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            handleErrorResponse(response);
        }

        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, AuthUserResponse.class);

        return objectMapper.readValue(response.getBody(), type);
    }

    @Override
    public AuthUserResponse getUserById(String authToken, String id) throws Exception {
        HttpEntity request = generateRequestEntity(authToken);

        ResponseEntity<String> response = restTemplate.exchange(
                new StringBuilder(audience).append(endpointUsers).append("/").append(id).toString(),
                HttpMethod.GET,
                request,
                String.class);

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            handleErrorResponse(response);
        }

        return objectMapper.readValue(response.getBody(), AuthUserResponse.class);
    }

    private HttpEntity generateRequestEntity(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);

        return new HttpEntity(headers);
    }

    //TODO Completar método handler errores
    private void handleErrorResponse(ResponseEntity<String> response) throws Exception {
        throw new Exception("Handlear errores");
    }

}
