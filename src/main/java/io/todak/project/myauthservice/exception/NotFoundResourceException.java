package io.todak.project.myauthservice.exception;

import lombok.Getter;

public class NotFoundResourceException extends RuntimeException {

    @Getter
    private final Object resource;

    public NotFoundResourceException(Object resource) {
        this.resource = resource;
    }
}
