package io.todak.project.myauthservice.exception;

import io.todak.project.myauthservice.controller.model.FieldErrorModel;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public class InvalidRequestException extends RuntimeException {

    @Getter
    private final List<FieldErrorModel> errors;

    public InvalidRequestException(List<FieldError> fieldErrors) {
        this.errors = fieldErrors.stream().map(FieldErrorModel::new).collect(Collectors.toList());
    }
}
