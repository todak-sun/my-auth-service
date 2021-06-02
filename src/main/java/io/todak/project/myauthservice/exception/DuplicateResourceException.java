package io.todak.project.myauthservice.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {

    private final String path;
    private final Object resource;

    public DuplicateResourceException(String path, Object resource) {
        this.path = path;
        this.resource = resource;
    }
}
