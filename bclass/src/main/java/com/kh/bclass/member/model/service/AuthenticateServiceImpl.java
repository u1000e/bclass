package com.kh.bclass.member.model.service;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.kh.bclass.exception.CustomAuthenticationException;
import com.kh.bclass.member.model.vo.Member;
import com.kh.bclass.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateServiceImpl implements AuthenticateService{

    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;
    private final TokenService tokenService;

    public Map<String, String> login(String userName, String password) {
        try {
            // 사용자 인증
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
           
            // 사용자 정보 조회
            Member member = memberService.getUserByUsername(userName);
            if (member == null) {
                throw new CustomAuthenticationException("존재하지 않는 사용자입니다.");
            }

            // JWT 토큰 생성 및 저장
            return tokenService.generateTokens(userName, member.getUserNo());
            
        } catch (AuthenticationException e) {
            log.error("권한부여 실패한 아이디 : {}", userName);
            throw new CustomAuthenticationException("비밀번호가 일치하지 않습니다.");
        }
    }

}
