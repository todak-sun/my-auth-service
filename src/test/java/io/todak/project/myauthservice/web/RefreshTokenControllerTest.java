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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RefreshTokenControllerTest extends MockMvcControllerBasement {

    @Autowired
    SignService signService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TokenProvider tokenProvider;

    @AfterEach
    public void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("refersh token을 성공적으로 발급받는 케이스")
    @Test
    public void refresh_token_success() throws Exception {
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "abcd@1234";

        Account account = signService.signUp(username, password);
        Token token = signService.signIn(username, password);

        String accessToken = generateToken(account.getId(), account.getUsername(), 1L);

        //when
        HttpHeaders httpHeaders = authHeader(accessToken);
        httpHeaders.put(TokenProvider.REFERESH_TOKEN_HEADER, List.of(token.getRefreshToken()));

        ResultActions perform = mvc.perform(
                post("/token/refresh")
                        .headers(httpHeaders)
        );

        //then
        assertWithResponseTemplateWithContent(perform)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.accessToken").exists())
                .andExpect(jsonPath("$.content.refreshToken").exists())
        ;
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