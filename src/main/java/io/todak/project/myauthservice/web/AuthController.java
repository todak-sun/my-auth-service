package io.todak.project.myauthservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {


    @GetMapping("/auth")
    public ResponseEntity<?> authenticate() {
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

}
