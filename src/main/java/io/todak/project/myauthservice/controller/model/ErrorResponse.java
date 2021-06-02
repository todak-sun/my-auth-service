package io.todak.project.myauthservice.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse<T> {

    private final T error;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;
    private final LocalDateTime transactionTime;

    @Builder
    private ErrorResponse(T error, String message) {
        this.error = error;
        this.message = message;
        this.transactionTime = LocalDateTime.now();
    }

    public static <T> ErrorResponse<T> of(T content) {
        return ErrorResponse.<T>builder()
                .error(content)
                .build();
    }

    public static <T> ErrorResponse<T> of(T content, String message) {
        return ErrorResponse.<T>builder()
                .error(content)
                .message(message)
                .build();
    }

    public static <T> ErrorResponse<T> message(String message) {
        return ErrorResponse.<T>builder()
                .message(message)
                .build();
    }

}
