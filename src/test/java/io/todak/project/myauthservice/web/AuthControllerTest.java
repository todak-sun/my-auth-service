package io.todak.project.myauthservice.web;

import io.todak.project.myauthservice.domain.Token;
import io.todak.project.myauthservice.service.SignService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends MockMvcControllerBasement {

    @Autowired
    SignService signService;

    @Test
    public void auth() throws Exception {
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "abcd@1234";

        signService.signUp(username, password);
        Token token = signService.signIn(username, password);

        //when
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.AUTHORIZATION, List.of("Bearer " + token.getAccessToken()));

        ResultActions perform = mvc.perform(MockMvcRequestBuilders
                .get("/auth")
                .headers(httpHeaders));

        //then
        perform.andExpect(status().isOk());
    }


}