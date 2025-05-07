package org.template.gateway.service.v1.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60; // 7일 (초 단위)

    private static final String KEY_PREFIX = "refreshToken:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Refresh Token 저장
    public void saveRefreshToken(String userCode, String refreshToken) {
        redisTemplate.opsForValue().set(KEY_PREFIX + userCode, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    // Refresh Token 조회
    public String getRefreshToken(String userCode) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + userCode);
    }

    // Refresh Token 삭제 (로그아웃 시 사용)
    public void deleteRefreshToken(String userCode) {
        redisTemplate.delete(KEY_PREFIX + userCode);
    }
}
