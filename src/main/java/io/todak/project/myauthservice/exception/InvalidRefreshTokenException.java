package io.todak.project.myauthservice.exception;

import lombok.Getter;

public class InvalidRefreshTokenException extends RuntimeException {

    @Getter
    private final String invalidToken;

    public InvalidRefreshTokenException(String invalidToken) {
        this.invalidToken = invalidToken;
    }
}
