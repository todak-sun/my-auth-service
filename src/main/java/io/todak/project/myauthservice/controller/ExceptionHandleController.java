package io.todak.project.myauthservice.controller;

import io.todak.project.myauthservice.controller.model.ErrorResponse;
import io.todak.project.myauthservice.controller.model.FieldErrorModel;
import io.todak.project.myauthservice.controller.model.SimpleErrorModel;
import io.todak.project.myauthservice.exception.DuplicateResourceException;
import io.todak.project.myauthservice.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandleController {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> invalidRequestException(InvalidRequestException exception) {

        var errors = exception.getErrors().stream()
                .map(FieldErrorModel::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(errors));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> duplicateResourceException(DuplicateResourceException exception) {

        var errors = SimpleErrorModel.of(exception.getPath(), exception.getResource());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(errors, "이미 존재하는 데이터입니다."));
    }

}
