package io.todak.project.myauthservice.exception;

import lombok.Getter;

public class DuplicateResourceException extends RuntimeException {

    @Getter
    private final Object resource;

    public DuplicateResourceException(Object resource) {
        this.resource = resource;
    }
}
