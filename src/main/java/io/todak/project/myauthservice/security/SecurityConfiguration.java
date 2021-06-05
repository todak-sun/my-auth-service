package io.todak.project.myauthservice.security;

import io.todak.project.myauthservice.jwt.JwtSecurityConfiguration;
import io.todak.project.myauthservice.jwt.TokenProvider;
import io.todak.project.myauthservice.repository.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers()
                .frameOptions()
                .sameOrigin();

        http.csrf()
                .disable();

        http.httpBasic()
                .disable()
                .formLogin()
                .disable();


        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/actuator/**", "/sign-up", "/sign-in")
                .permitAll();

        http.authorizeRequests()
                .anyRequest().authenticated();

        http.apply(new JwtSecurityConfiguration(tokenProvider, refreshTokenRepository));
    }
}
