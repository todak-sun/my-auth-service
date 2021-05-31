package io.todak.project.myauthservice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Getter
    private String password;

    @Builder
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // 비즈니스 메서드

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
