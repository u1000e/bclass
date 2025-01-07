package com.kh.bclass.member.model.service;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kh.bclass.exception.CustomAuthenticationException;
import com.kh.bclass.member.model.vo.CustomUserDetails;
import com.kh.bclass.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateServiceImpl implements AuthenticateService{

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public Map<String, String> login(String userName, String password) {
        try {
            // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
            		new UsernamePasswordAuthenticationToken(userName, password)
            );
           
            // 사용자 정보 조회
            CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();

            // JWT 토큰 생성 및 저장
            return tokenService.generateTokens(userName, user.getUserNo());
            
        } catch (AuthenticationException e) {
            log.error("인증에 실패한 아이디 : {}, 이유 : {}", userName, e.getMessage(), e);
            throw new CustomAuthenticationException("사용자 이름 또는 비밀번호가 일치하지 않습니다.");
        }
    }

}
