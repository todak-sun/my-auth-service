package io.todak.project.myauthservice.web;

import io.todak.project.myauthservice.web.model.SignUpModel;
import io.todak.project.myauthservice.web.model.request.LoginRequest;
import io.todak.project.myauthservice.web.model.response.Response;
import io.todak.project.myauthservice.web.model.response.TokenResponse;
import io.todak.project.myauthservice.web.validator.SignValidator;
import io.todak.project.myauthservice.domain.Token;
import io.todak.project.myauthservice.entity.Account;
import io.todak.project.myauthservice.exception.InvalidRequestException;
import io.todak.project.myauthservice.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SignController {

    private final SignService signService;
    private final SignValidator signValidator;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest request, Errors errors) {

        signValidator.validate(request, errors);

        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors.getFieldErrors());
        }

        Token token = signService.signIn(request.getUsername(), request.getPassword());

        TokenResponse response = new TokenResponse(token.getAccessToken(), token.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.of(response));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpModel.Req request, Errors errors) {

        signValidator.validate(request, errors);

        if (errors.hasErrors()) {
            throw new InvalidRequestException(errors.getFieldErrors());
        }

        Account account = signService.signUp(request.getUsername(), request.getPassword());

        SignUpModel.Res response = new SignUpModel.Res(account.getId(), account.getUsername(), account.getCreatedDateTime());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.of(response));
    }


}
