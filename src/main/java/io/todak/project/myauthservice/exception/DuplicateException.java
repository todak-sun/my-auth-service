package io.todak.project.myauthservice.exception;

import lombok.Getter;

public class DuplicateException extends RuntimeException {

    @Getter
    private final Object resource;

    public DuplicateException(Object resource) {
        this.resource = resource;
    }
}
