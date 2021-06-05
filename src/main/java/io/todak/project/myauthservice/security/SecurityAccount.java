package io.todak.project.myauthservice.security;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class SecurityAccount extends User {

    @Getter
    private final Long userId;

    public SecurityAccount(Long userId, String username) {
        super(username, "", Collections.emptyList());
        this.userId = userId;
    }

}
