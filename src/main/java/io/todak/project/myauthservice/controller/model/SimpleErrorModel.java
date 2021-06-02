package io.todak.project.myauthservice.controller.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SimpleErrorModel {

    private final String path;
    private final Object value;

    @Builder(access = AccessLevel.PRIVATE)
    private SimpleErrorModel(String path, Object value) {
        this.path = path;
        this.value = value;
    }

    public static SimpleErrorModel of(String path, Object value) {
        return SimpleErrorModel.builder()
                .path(path)
                .value(value)
                .build();
    }

}
