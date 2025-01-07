package com.kh.bclass.token.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.bclass.configuration.jwt.JwtUtil;
import com.kh.bclass.member.model.service.MemberService;
import com.kh.bclass.member.model.vo.Member;
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
    private final MemberService service;

    @Transactional
    public Map<String, String> generateTokens(String userName, Long userNo) {
    	
        deleteExpiredRefreshTokens(userNo);
        return createTokens(userName, userNo);
    }

    @Transactional
    public Map<String, String> refreshAccessToken(String refreshToken) {
    	
        RefreshToken refreshTokenEntity = refreshTokenMapper.findByToken(refreshToken);
        
        if (refreshTokenEntity == null || refreshTokenEntity.getExpiration() < System.currentTimeMillis()) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        String userName = jwtUtil.parseJwt(refreshToken).getSubject();
        Member member = service.getUserByUsername(userName);
        deleteExpiredRefreshTokens(member.getUserNo());

        return createTokens(userName, member.getUserNo());
    }
    
    private Map<String, String> createTokens(String userName, Long userNo) {
    	
        String accessToken = jwtUtil.generateAccessToken(userName);
        String refreshToken = jwtUtil.generateRefreshToken(userName);

        RefreshToken newRefreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .expiration(System.currentTimeMillis() + 604800000) // 7일
                .userNo(userNo)
                .build();
        refreshTokenMapper.save(newRefreshTokenEntity);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        log.info("새 토큰 생성: {}", userName);
        return tokens;
        
    }

    private void deleteExpiredRefreshTokens(Long userNo) {
    	
        Map<String, Long> param = new HashMap<>();
        param.put("userNo", userNo);
        param.put("currentTimeMillis", System.currentTimeMillis());
        
        refreshTokenMapper.deleteExpiredTokens(param);
        log.info("만료된 리프레시 토큰 삭제 완료");
    }
    
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenMapper.deleteByToken(refreshToken);
        log.info("Refresh Token 삭제 완료: {}", refreshToken);
    }

}
