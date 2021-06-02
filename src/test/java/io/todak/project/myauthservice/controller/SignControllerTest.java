package io.todak.project.myauthservice.controller;

import io.todak.project.myauthservice.service.SignService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class SignControllerTest extends MockMvcControllerBasement {

    private final static String VALID_USERNAME = "validate@username.com";
    private final static String INVALID_USERNAME = "username";
    private final static String VALID_PASSWORD = "password@1234";
    private final static String VALID_ANOTHER_PASSWORD = "password@12345";
    private final static String INVALID_PASSWORD = "pwd";

    private final static String SIGN_UP = "/sign-up";
    private final static String SIGN_IN = "/sign-in";

    @Autowired
    private SignService signService;


    @DisplayName("회원가입 성공 테스트")
    @Test
    public void sign_up_success() throws Exception {
        //given
        Map<String, Object> requestMap = getUserMap(VALID_USERNAME, VALID_PASSWORD, VALID_PASSWORD);

        String requestBody = objectMapper.writeValueAsString(requestMap);

        //when
        ResultActions perform = mvc.perform(post(SIGN_UP)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        assertWithResponseTemplate(perform)
                .andExpect(status().isCreated())
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
    public void sign_up_fail_diffrent_password() throws Exception {
        //given
        String password = VALID_PASSWORD;
        String passwordRe = VALID_ANOTHER_PASSWORD;

        Map<String, Object> requestMap = getUserMap(VALID_USERNAME, password, passwordRe);

        String requestBody = objectMapper.writeValueAsString(requestMap);

        //when
        ResultActions perform = mvc.perform(post(SIGN_UP)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        assertWithErrorTemplate(perform)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0]").exists())
                .andExpect(jsonPath("$.error[0]").isMap())
                .andExpect(jsonPath("$.error[0].field").exists())
                .andExpect(jsonPath("$.error[0].field").isString())
                .andExpect(jsonPath("$.error[0].field").value("passwordRe"))
                .andExpect(jsonPath("$.error[0].rejectedValue").exists())
                .andExpect(jsonPath("$.error[0].rejectedValue").isString())
                .andExpect(jsonPath("$.error[0].rejectedValue").value(passwordRe))
                .andExpect(jsonPath("$.error[0].message").exists())
                .andExpect(jsonPath("$.error[0].message").isString());
    }

    @DisplayName("회원가입 실패 테스트 - email 형식이 아닌 사용자 계정.")
    @Test
    public void sign_up_fail_invalid_username() throws Exception {
        //given
        String username = INVALID_USERNAME;

        Map<String, Object> requestMap = getUserMap(username, VALID_PASSWORD, VALID_PASSWORD);

        String requestBody = objectMapper.writeValueAsString(requestMap);

        //when
        ResultActions perform = mvc.perform(post(SIGN_UP)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        assertWithErrorTemplate(perform)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0]").exists())
                .andExpect(jsonPath("$.error[0]").isMap())
                .andExpect(jsonPath("$.error[0].field").exists())
                .andExpect(jsonPath("$.error[0].field").isString())
                .andExpect(jsonPath("$.error[0].field").value("username"))
                .andExpect(jsonPath("$.error[0].rejectedValue").exists())
                .andExpect(jsonPath("$.error[0].rejectedValue").isString())
                .andExpect(jsonPath("$.error[0].rejectedValue").value(username))
                .andExpect(jsonPath("$.error[0].message").exists())
                .andExpect(jsonPath("$.error[0].message").isString());
    }

    @DisplayName("회원가입 실패 테스트 - 유효성을 통과하지 못한 비밀번호")
    @Test
    public void sign_up_fail_invalid_password() throws Exception {
        //given
        String password = INVALID_PASSWORD;
        String passwordRe = INVALID_PASSWORD;

        Map<String, Object> requestMap = getUserMap(VALID_USERNAME, password, passwordRe);

        String requestBody = objectMapper.writeValueAsString(requestMap);

        //when
        ResultActions perform = mvc.perform(post(SIGN_UP)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        assertWithErrorTemplate(perform)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0]").exists())
                .andExpect(jsonPath("$.error[0]").isMap())
                .andExpect(jsonPath("$.error[0].field").exists())
                .andExpect(jsonPath("$.error[0].field").isString())
                .andExpect(jsonPath("$.error[0].field").value("password"))
                .andExpect(jsonPath("$.error[0].rejectedValue").exists())
                .andExpect(jsonPath("$.error[0].rejectedValue").isString())
                .andExpect(jsonPath("$.error[0].rejectedValue").value(password))
                .andExpect(jsonPath("$.error[0].message").exists())
                .andExpect(jsonPath("$.error[0].message").isString());
    }

    @DisplayName("회원가입 실패 테스트 - 중복된 아이디로 가입을 원하는 경우")
    @Test
    public void sign_up_fail_duplicate_username() throws Exception {
        //TODO: 테스트 수정
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "pwd";
        String passwordRe = "pwd";

        Map<String, Object> requestMap = getUserMap(username, password, passwordRe);

        String requestBody = objectMapper.writeValueAsString(requestMap);

        //when
        ResultActions perform = mvc.perform(post(SIGN_UP)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        assertWithErrorTemplate(perform)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0]").exists())
                .andExpect(jsonPath("$.error[0]").isMap())
                .andExpect(jsonPath("$.error[0].field").exists())
                .andExpect(jsonPath("$.error[0].field").isString())
                .andExpect(jsonPath("$.error[0].field").value("password"))
                .andExpect(jsonPath("$.error[0].rejectedValue").exists())
                .andExpect(jsonPath("$.error[0].rejectedValue").isString())
                .andExpect(jsonPath("$.error[0].rejectedValue").value(password))
                .andExpect(jsonPath("$.error[0].message").exists())
                .andExpect(jsonPath("$.error[0].message").isString());
    }

    private Map<String, Object> getUserMap(String username, String password, String passwordRe) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("passwordRe", passwordRe);
        return map;
    }


}
