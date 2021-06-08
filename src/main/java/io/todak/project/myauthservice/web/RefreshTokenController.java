package io.todak.project.myauthservice.web;

import io.todak.project.myauthservice.domain.Token;
import io.todak.project.myauthservice.jwt.TokenProvider;
import io.todak.project.myauthservice.service.RefreshTokenService;
import io.todak.project.myauthservice.web.model.response.Response;
import io.todak.project.myauthservice.web.model.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/token/refresh")
    public ResponseEntity<?> tokenRefresh(@RequestHeader(name = TokenProvider.REFERESH_TOKEN_HEADER, required = false) String refreshToken) {
//        if (Objects.isNull(refreshToken)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .build();
//        }

        Token token = refreshTokenService.generateNewTokenIfValidRefreshToken(refreshToken);
        TokenResponse response = new TokenResponse(token.getAccessToken(), token.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.of(response));
    }

}
