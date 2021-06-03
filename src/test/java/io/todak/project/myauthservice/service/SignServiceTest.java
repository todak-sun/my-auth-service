package io.todak.project.myauthservice.service;

import io.todak.project.myauthservice.domain.Token;
import io.todak.project.myauthservice.entity.Account;
import io.todak.project.myauthservice.exception.DuplicateResourceException;
import io.todak.project.myauthservice.exception.InvalidPasswordException;
import io.todak.project.myauthservice.exception.UsernameNotFoundException;
import io.todak.project.myauthservice.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SignServiceTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SignService signService;

    @AfterEach
    public void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("빈으로 등록되어 있다.")
    @Test
    public void is_bean() {
        assertNotNull(signService, "signService는 null이 아니다.");

        SignService beanSignService = context.getBean(SignService.class);
        assertEquals(signService, beanSignService, "스프링 Bean으로 등록되어 있다.");
    }

    @DisplayName("회원가입 -  성공 테스트")
    @Test
    public void sign_up_test_success() {
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "password";

        //when
        Account newAccount = signService.signUp(username, password);

        //then
        assertNotNull(newAccount.getId(), () -> "새로 부여된 id가 있다.");
        assertNotNull(newAccount.getUsername(), () -> "username은 null이 아니다.");
        assertNotNull(newAccount.getCreatedDateTime(), () -> "회원가입한 시간이 기록된다.");
        assertNotNull(newAccount.getLastModifiedDateTime(), () -> "회원가입한 시간이 updateDateTime에도 함께 기록된다.");

        log.info("id : {}", newAccount.getId());
    }

    @DisplayName("회원가입 - 중복된 아이디로 회원가입을 시도하면 적절한 에러를 뱉어내는 테스트")
    @Test
    public void sign_up_test_duplicated_username() {
        // given
        String username = "tjsdydwn@gmail.com";
        String password = "password";

        // when
        Account first = signService.signUp(username, password);

        // then
        DuplicateResourceException duplicateResourceException = assertThrows(
                DuplicateResourceException.class,
                () -> signService.signUp(username, password),
                () -> "중복된 username 사용하면 DuplicateResourceException 터진다.");

        String path = duplicateResourceException.getPath();
        Object resource = duplicateResourceException.getResource();

        assertEquals(path, "username", "중복된 데이터가 어떤 데이터인지 알려준다.");
        assertEquals(resource, username, () -> "중복된 resource의 값을 알려준다.");
    }

    @DisplayName("로그인 - 존재하지 않는 ID로 로그인하면, 적절한 에러를 뱉어낸다.")
    @Test
    public void sign_in_with_not_exist_username() {
        // given
        String username = "NOTEXISTS";
        String password = "password";

        //when & then
        UsernameNotFoundException usernameNotFoundException = assertThrows(
                UsernameNotFoundException.class,
                () -> signService.signIn(username, password),
                () -> "존재하지 않는 사용자가 로그인을 시도하면 UsernameNotFoundException");
    }

    @DisplayName("로그인 - 비밀번호가 틀리면 적절한 에러를 뱉어낸다.")
    @Test
    public void sign_in_with_wrong_password() {
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "password";
        signService.signUp(username, password);

        //when & then
        assertThrows(
                InvalidPasswordException.class,
                () -> signService.signIn(username, "diffrent password"),
                () -> "password가 틀리면 InvalidPasswordException이 발생한다.");
    }

    @DisplayName("로그인 - 성공 테스트")
    @Test
    public void sign_in_success() {
        //given
        String username = "tjsdydwn@gmail.com";
        String password = "password";
        signService.signUp(username, password);

        //when
        Token token = signService.signIn(username, password);

        //then
        assertNotNull(token.getAccessToken(), () -> "로그인에 성공하면 access token을 발급받는다.");
        assertNotNull(token.getRefreshToken(), () -> "로그인에 성공하면 refresh token을 발급받는다.");
    }


}