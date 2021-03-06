package io.todak.project.myauthservice.web;

import io.todak.project.myauthservice.domain.Token;
import io.todak.project.myauthservice.entity.Account;
import io.todak.project.myauthservice.jwt.TokenProvider;
import io.todak.project.myauthservice.repository.AccountRepository;
import io.todak.project.myauthservice.service.SignService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends MockMvcControllerBasement {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    SignService signService;

    @Autowired
    AccountRepository accountRepository;

    @AfterEach
    public void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("존재하는 access_token으로 인증에 통과한 경우")
    @Test
    public void auth_success_with_access_token() throws Exception {
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "abcd@1234";

        signService.signUp(username, password);
        Token token = signService.signIn(username, password);

        //when
        ResultActions perform = mvc.perform(get("/auth")
                .headers(authHeader(token.getAccessToken())));

        //then
        perform.andExpect(status().isOk());
    }

    @DisplayName("access_token이 expired 된 경우")
    @Test
    public void auth_success_then_recive_new_tokens() throws Exception {
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "abcd@1234";

        Account account = signService.signUp(username, password);
        signService.signIn(username, password);

        String accessToken = generateToken(account.getId(), account.getUsername(), 1L);

        //when
        ResultActions perform = mvc.perform(get("/auth").
                headers(authHeader(accessToken)));

        //then
        perform.andExpect(status().isForbidden());
    }

    private HttpHeaders authHeader(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.AUTHORIZATION, List.of("Bearer " + accessToken));
        return httpHeaders;
    }

    private String generateToken(Long userId, String username, long expired) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method createToken = TokenProvider.class.getDeclaredMethod("createToken", Long.class, String.class, long.class);
        createToken.setAccessible(true);
        return (String) createToken.invoke(tokenProvider, userId, username, expired);
    }


}