package io.todak.project.myauthservice.web.model.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SimpleErrorResponse {

    private final String path;
    private final Object value;

    @Builder(access = AccessLevel.PRIVATE)
    private SimpleErrorResponse(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    public static SimpleErrorResponse of(String path, Object value) {
        return SimpleErrorResponse.builder()
                .path(path)
                .value(value)
                .build();
    }

}
