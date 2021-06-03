package io.todak.project.myauthservice.web.model.response;

import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class FieldErrorResponse {

    private String field;
    private Object rejectedValue;
    private String message;

    public FieldErrorResponse(FieldError error) {
        this.field = error.getField();
        this.rejectedValue = error.getRejectedValue();
        this.message = error.getDefaultMessage();
    }
}
