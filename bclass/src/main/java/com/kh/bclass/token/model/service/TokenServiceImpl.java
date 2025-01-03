package com.kh.bclass.token.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kh.bclass.configuration.jwt.JwtUtil;
import com.kh.bclass.token.model.dao.RefreshTokenMapper;
import com.kh.bclass.token.model.vo.RefreshToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService{

    private final JwtUtil jwtUtil;
    private final RefreshTokenMapper refreshTokenMapper;

    public Map<String, String> generateTokens(String userName, Long userNo) {
        String accessToken = jwtUtil.generateAccessToken(userName);
        String refreshToken = jwtUtil.generateRefreshToken(userName);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .expiration(System.currentTimeMillis() + 604800000) // 7일 유효
                .userNo(userNo)
                .build();
        refreshTokenMapper.save(refreshTokenEntity);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        log.info("토큰 생성 성공: {}", userName);
        return tokens;
    }

    public Map<String, String> refreshAccessToken(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenMapper.findByToken(refreshToken);
        if (refreshTokenEntity == null || refreshTokenEntity.getExpiration() < System.currentTimeMillis()) {
            throw new RuntimeException("Invalid refresh token");
        }

        String userName = jwtUtil.parseJwt(refreshToken).getSubject();
        String newAccessToken = jwtUtil.generateAccessToken(userName);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", refreshToken); // 기존 리프레시 토큰 반환

        log.info("새로운 액세스 토큰 발급: {}", userName);
        return tokens;
    }

}
