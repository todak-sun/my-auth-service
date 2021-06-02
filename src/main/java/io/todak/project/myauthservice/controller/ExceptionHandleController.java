package io.todak.project.myauthservice.controller;

import io.todak.project.myauthservice.controller.model.ErrorResponse;
import io.todak.project.myauthservice.controller.model.FieldErrorModel;
import io.todak.project.myauthservice.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionHandleController {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> invalidRequestException(InvalidRequestException exception) {

        List<FieldErrorModel> errors = exception.getErrors();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(errors));
    }

}
