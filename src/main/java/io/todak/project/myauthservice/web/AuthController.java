package io.todak.project.myauthservice.web;

import io.todak.project.myauthservice.domain.Token;
import io.todak.project.myauthservice.security.SecurityAccount;
import io.todak.project.myauthservice.security.SignedUser;
import io.todak.project.myauthservice.service.AuthService;
import io.todak.project.myauthservice.web.model.response.Response;
import io.todak.project.myauthservice.web.model.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth")
    public ResponseEntity<?> authenticate(
            @SignedUser SecurityAccount account,
            @RequestHeader(name = "Refresh-Token", required = false) String refreshToken) {

        if (account != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }

        if (refreshToken != null) {
            Token token = authService.generateNewTokenIfValidRefreshToken(refreshToken);
            TokenResponse response = new TokenResponse(token.getAccessToken(), token.getRefreshToken());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.of(response));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
    }

}
