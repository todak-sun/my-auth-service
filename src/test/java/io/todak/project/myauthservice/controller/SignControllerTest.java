package io.todak.project.myauthservice.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SignControllerTest extends MockMvcControllerBasement {

    private final static String SIGN_UP = "/sign-up";
    private final static String SIGN_IN = "/sign-in";

    @DisplayName("회원가입 성공 테스트")
    @Test
    public void sign_up_success() throws Exception {
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "password";
        String passwordRe = "password";

        Map<String, Object> requestMap = getUserMap(username, password, passwordRe);

        String requestBody = objectMapper.writeValueAsString(requestMap);

        //when
        ResultActions perform = mvc.perform(post(SIGN_UP)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        perform.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionTime").exists())
                .andExpect(jsonPath("$.transactionTime").isString())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isMap())
                .andExpect(jsonPath("$.content.userId").exists())
                .andExpect(jsonPath("$.content.userId").isNumber())
                .andExpect(jsonPath("$.content.username").exists())
                .andExpect(jsonPath("$.content.username").isString())
                .andExpect(jsonPath("$.content.enrolledAt").exists())
                .andExpect(jsonPath("$.content.enrolledAt").isString());
    }

    @DisplayName("회원가입 실패 테스트 - password 와 passwordRe의 불일치.")
    @Test
    public void sign_up_fail() throws Exception {
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "password";
        String passwordRe = "diffrent";

        Map<String, Object> requestMap = getUserMap(username, password, passwordRe);

        String requestBody = objectMapper.writeValueAsString(requestMap);

        //when
        ResultActions perform = mvc.perform(post(SIGN_UP)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        perform.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }

    private Map<String, Object> getUserMap(String username, String password, String passwordRe) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("passwordRe", passwordRe);
        return map;
    }

}