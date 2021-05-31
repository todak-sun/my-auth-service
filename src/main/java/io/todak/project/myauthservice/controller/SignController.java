package io.todak.project.myauthservice.controller;

import io.todak.project.myauthservice.controller.model.LoginRequest;
import io.todak.project.myauthservice.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SignController {

    private final SignService signService;

    @PostMapping("sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest login) {

        signService.signIn(login.getUsername(), login.getPassword());

        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }


}
