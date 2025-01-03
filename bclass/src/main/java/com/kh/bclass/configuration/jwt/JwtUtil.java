package com.kh.bclass.configuration.jwt;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Bean
    public SecretKey secretKey() {
        return this.key;
    }
    
	public String generateAccessToken(String userName){
        // 인증 정보에서 사용자 이름 추출
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + 3600000)) // 3일 유효
                //.expiration(new Date((new Date()).getTime() + 60000)) // 1분 유효
                .signWith(key)
                .compact();
    }
	
    public String generateRefreshToken(String userName) {
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + 604800000)) // 7일 유효
                //.expiration(new Date((new Date()).getTime() + 240000)) // 4분 유효
                .signWith(key)
                .compact();
    }
	
    public Claims parseJwt(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    


}
