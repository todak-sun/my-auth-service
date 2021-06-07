package io.todak.project.myauthservice.web;

import io.todak.project.myauthservice.security.SecurityAccount;
import io.todak.project.myauthservice.security.SignedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    @GetMapping("/auth")
    public ResponseEntity<?> authenticate(@SignedUser SecurityAccount account) {
        if (Objects.isNull(account)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }


}
