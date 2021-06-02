package io.todak.project.myauthservice.controller.model;

import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class FieldErrorModel {

    private String field;
    private Object rejectedValue;
    private String message;

    public FieldErrorModel(FieldError error) {
        this.field = error.getField();
        this.rejectedValue = error.getRejectedValue();
        this.message = error.getDefaultMessage();
    }
}
