package io.todak.project.myauthservice.controller.model;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
