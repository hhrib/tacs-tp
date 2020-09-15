package net.tacs.game.model;

import java.util.Objects;

public class ApiError {

    private String code;
    private String detail;

    public ApiError(String code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "code='" + code + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiError apiError = (ApiError) o;
        return code.equals(apiError.code) &&
                detail.equals(apiError.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, detail);
    }
}
