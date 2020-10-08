package net.tacs.game.model.dto;

import java.util.Objects;

public class NextUserTurnDTO {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NextUserTurnDTO that = (NextUserTurnDTO) o;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "NextUserTurnDTO{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
