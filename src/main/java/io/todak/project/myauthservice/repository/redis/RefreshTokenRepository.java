package io.todak.project.myauthservice.repository.redis;

import io.todak.project.myauthservice.jwt.JwtConfigurationProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class RefreshTokenRepository {

    private final RedisTemplate<String, Long> redisTemplate;
    private final JwtConfigurationProperty property;

    private ValueOperations<String, Long> valueOps;

    @PostConstruct
    public void setUp() {
        this.valueOps = redisTemplate.opsForValue();
    }

    public void save(String token, Long id) {
        this.valueOps.set(token, id, Duration.ofMillis(property.getRefreshTokenValidityTime()));
    }

    public Optional<Long> findUserIdByToken(String token) {
        return Optional.ofNullable(this.valueOps.get(token));
    }

}
