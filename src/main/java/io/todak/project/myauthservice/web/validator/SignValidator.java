package io.todak.project.myauthservice.web.validator;

import io.todak.project.myauthservice.web.model.request.LoginRequest;
import io.todak.project.myauthservice.web.model.SignUpModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Pattern;

@Component
public class SignValidator {

    private final Pattern E_MAIL = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
    private final Pattern PASSWORD = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$");

    public void validate(LoginRequest loginRequest, Errors errors) {

        if (isInvalidEmail(loginRequest.getUsername())) {
            errors.rejectValue("username",
                    "wrongValue",
                    "계정은 이메일 형식만을 허용합니다.");
        }

        if (isInvalidPassword(loginRequest.getPassword())) {
            errors.rejectValue(
                    "password",
                    "wrongValue",
                    "비밀번호는 8 ~ 16길이로 문자, 숫자, 특수문자가 각각 한 개 이상 반드시 포함되어야 합니다.");
        }
    }

    public void validate(SignUpModel.Req signUpRequest, Errors errors) {
        if (isInvalidEmail(signUpRequest.getUsername())) {
            errors.rejectValue("username",
                    "wrongValue",
                    "계정은 이메일 형식만을 허용합니다.");
        }
        if (isInvalidPassword(signUpRequest.getPassword())) {
            errors.rejectValue(
                    "password",
                    "wrongValue",
                    "비밀번호는 8 ~ 16길이로 문자, 숫자, 특수문자가 각각 한 개 이상 반드시 포함되어야 합니다.");
        }
        if (signUpRequest.isDiffrentPassword()) {
            errors.rejectValue(
                    "passwordRe",
                    "wrongValue",
                    "비밀번호가 일치하지 않습니다.");
        }
    }

    private boolean isInvalidEmail(String email) {
        if (email == null) return false;
        return !E_MAIL.matcher(email).matches();
    }

    private boolean isInvalidPassword(String password) {
        if (password == null) return false;
        return !PASSWORD.matcher(password).matches();
    }

}
