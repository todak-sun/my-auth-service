package io.todak.project.myauthservice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverrides({
        @AttributeOverride(name = "createdDateTime",
                column = @Column(name = "ENROLLED_DATE_TIME")),
        @AttributeOverride(name = "lastModifiedDateTime",
                column = @Column(name = "UPDATED_DATE_TIME"))
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Account extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true, nullable = false)
    @Getter
    private String username;

    @Column(unique = true, nullable = false)
    private String password;

    @Builder(access = AccessLevel.PRIVATE)
    private Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Account create(String username, String password, PasswordEncoder passwordEncoder) {
        return Account.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
    }

    // 비즈니스 메서드
    public boolean hasSamePassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }

    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(newPassword);
    }
}
