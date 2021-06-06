package io.todak.project.myauthservice.security;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class SecurityAccount extends User {

    @Getter
    private final Long userId;
    @Getter
    private final SecurityAccount account;

    public SecurityAccount(Long userId, String username) {
        super(username, "", Collections.emptyList());
        this.userId = userId;
        account = this;
    }

}
