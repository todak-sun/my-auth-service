package io.todak.project.myauthservice.web;

import io.todak.project.myauthservice.exception.InvalidPasswordException;
import io.todak.project.myauthservice.exception.UsernameNotFoundException;
import io.todak.project.myauthservice.web.model.response.ErrorResponse;
import io.todak.project.myauthservice.web.model.response.FieldErrorResponse;
import io.todak.project.myauthservice.web.model.response.SimpleErrorResponse;
import io.todak.project.myauthservice.exception.DuplicateResourceException;
import io.todak.project.myauthservice.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandleController {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> invalidRequestException(InvalidRequestException exception) {
        log.debug(exception.getMessage());
        var errors = exception.getErrors().stream()
                .map(FieldErrorResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(errors));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> duplicateResourceException(DuplicateResourceException exception) {
        log.debug(exception.getMessage());
        var errors = SimpleErrorResponse.of(exception.getPath(), exception.getResource());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(errors, "이미 존재하는 데이터입니다."));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFoundException(UsernameNotFoundException exception) {
        log.debug(exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.message("존재하지 않는 계정이거나 패스워드가 일치하지 않습니다."));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> invalidPasswordException(InvalidPasswordException exception) {
        log.debug(exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.message("존재하지 않는 계정이거나 패스워드가 일치하지 않습니다."));
    }

}
