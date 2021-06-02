package io.todak.project.myauthservice.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

public class InvalidRequestException extends RuntimeException {

    @Getter
    private final List<FieldError> errors;

    public InvalidRequestException(List<FieldError> fieldErrors) {
        this.errors = fieldErrors;
    }
}
