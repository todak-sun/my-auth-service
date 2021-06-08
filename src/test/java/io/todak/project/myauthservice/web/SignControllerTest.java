package io.todak.project.myauthservice.web;

import io.todak.project.myauthservice.jwt.TokenProvider;
import io.todak.project.myauthservice.repository.AccountRepository;
import io.todak.project.myauthservice.service.SignService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
    TokenProvider tokenProvider;

    @Autowired
    private SignService signService;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void afterEach() {
        accountRepository.deleteAll();
    }

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
        assertWithResponseTemplateWithContent(perform)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").isMap())
                .andExpect(jsonPath("$.content.userId").exists())
                .andExpect(jsonPath("$.content.userId").isNumber())
                .andExpect(jsonPath("$.content.username").exists())
                .andExpect(jsonPath("$.content.username").isString())
                .andExpect(jsonPath("$.content.enrolledAt").exists())
                .andExpect(jsonPath("$.content.enrolledAt").isString())
                .andDo(document(
                        "",
                        requestBody(requestMap),
                        responseBody(),
                        requestFields(
                                fieldWithPath("username").description("계정명").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("비밀번호").type(JsonFieldType.STRING),
                                fieldWithPath("passwordRe").description("중복 확인용 비밀번호 재입력").type(JsonFieldType.STRING)

                        ),
                        responseFields(
                                fieldWithPath("content").description("응답").type(JsonFieldType.OBJECT),
                                fieldWithPath("content.userId").description("사용자 식별값").type(JsonFieldType.NUMBER),
                                fieldWithPath("content.username").description("사용자 계정명").type(JsonFieldType.STRING),
                                fieldWithPath("content.enrolledAt").description("회원가입일시").type(JsonFieldType.STRING),
                                fieldWithPath("transactionTime").description("응답시간").type(JsonFieldType.STRING)
                        )

                ))
        ;
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

        assertWithErrorTemplateWithDetailError(perform)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isArray())
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

        assertWithErrorTemplateWithDetailError(perform)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isArray())
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

        assertWithErrorTemplateWithDetailError(perform)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isArray())
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
        //given
        String username = VALID_USERNAME;
        String password = VALID_PASSWORD;

        signService.signUp(username, password);

        Map<String, Object> requestMap = getUserMap(username, password, VALID_PASSWORD);

        //when
        ResultActions perform = mvc.perform(post(SIGN_UP)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap)));

        assertWithErrorTemplateWithDetailError(perform)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error").isMap())
                .andExpect(jsonPath("$.error.path").exists())
                .andExpect(jsonPath("$.error.path").isString())
                .andExpect(jsonPath("$.error.path").value("username"))
                .andExpect(jsonPath("$.error.value").exists())
                .andExpect(jsonPath("$.error.value").value(username))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").isString());
    }

    @DisplayName("로그인 성공 테스트")
    @Test
    public void sign_in_success() throws Exception {
        // TODO: TokenProvider 까서, 내용까지 확인해볼 것.
        //given
        String username = VALID_USERNAME;
        String password = VALID_PASSWORD;
        signService.signUp(username, password);

        //when
        ResultActions perform = mvc.perform(post(SIGN_IN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getLoginMap(username, password)))
        );

        //then
        assertWithResponseTemplateWithContent(perform)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.refreshToken").exists())
                .andExpect(jsonPath("$.content.accessToken").exists());
    }

    @DisplayName("로그인 실패 테스트 - 비밀번호가 틀렸을 때")
    @Test
    public void sign_in_fail_with_invalid_password() throws Exception {
        //given
        String username = VALID_USERNAME;
        signService.signUp(username, VALID_PASSWORD);

        //when
        ResultActions perform = mvc.perform(post(SIGN_IN)
                .content(objectMapper.writeValueAsString(getLoginMap(username, VALID_ANOTHER_PASSWORD)))
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        assertWithErrorDefaultTemplate(perform)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").isString());
    }

    @DisplayName("로그인 실패 테스트 - 없는 계정으로 로그인 시도 할 때")
    @Test
    public void sign_in_fail_with_username_not_existed() throws Exception {

        //when
        ResultActions perform = mvc.perform(post(SIGN_IN)
                .content(objectMapper.writeValueAsString(getLoginMap(VALID_USERNAME, VALID_ANOTHER_PASSWORD)))
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        assertWithErrorDefaultTemplate(perform)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").isString());
    }


    private Map<String, Object> getLoginMap(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return map;
    }

    private Map<String, Object> getUserMap(String username, String password, String passwordRe) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("passwordRe", passwordRe);
        return map;
    }


}
