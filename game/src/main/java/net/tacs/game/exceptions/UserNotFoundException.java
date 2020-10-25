package net.tacs.game.exceptions;

import net.tacs.game.model.ApiError;
import org.springframework.http.HttpStatus;

import java.util.List;

public class UserNotFoundException extends Exception {
    private HttpStatus httpStatus;
    private List<ApiError> apiErrors;

    public UserNotFoundException(HttpStatus httpStatus, List<ApiError> apiErrors) {
        this.httpStatus = httpStatus;
        this.apiErrors = apiErrors;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public List<ApiError> getApiErrors() {
        return apiErrors;
    }
}
