package io.todak.project.myauthservice.controller.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class SignUpModel {

    @Getter
    @Setter
    public static class Req {
        private String username;
        private String password;
        private String passwordRe;

        public boolean isDiffrentPassword() {
            return !this.password.equals(passwordRe);
        }
    }

    @Getter
    public static class Res {
        private Long userId;
        private String username;
        private LocalDateTime enrolledAt;

        public Res(Long userId, String username, LocalDateTime enrolledAt) {
            this.userId = userId;
            this.username = username;
            this.enrolledAt = enrolledAt;
        }
    }


}
