package io.todak.project.myauthservice.web.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Response<T> {

    private final T content;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;
    private final LocalDateTime transactionTime;

    @Builder
    private Response(T content, String message) {
        this.content = content;
        this.message = message;
        this.transactionTime = LocalDateTime.now();
    }

    public static <T> Response<T> of(T content) {
        return Response.<T>builder()
                .content(content)
                .build();
    }

    public static <T> Response<T> of(T content, String message) {
        return Response.<T>builder()
                .content(content)
                .message(message)
                .build();
    }

}
