package com.kh.bclass.configuration.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kh.bclass.exception.AccessTokenExpiredException;
import com.kh.bclass.exception.TokenSubjectMismatchException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if (request.getRequestURI().equals("/member/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("authorization = " + authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("authorization 이 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // Token 꺼내기
        String token = authorization.split(" ")[1];
        // Token Expired 되었는지 여부
        try {
            Claims claims = jwtUtil.parseJwt(token);

            // 토큰 검증 성공 시, 필요한 로직 추가
            // 예: SecurityContextHolder에 인증 정보 설정
            String username = claims.getSubject();

            // 사용자 정보 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
 

            // 권한 설정
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContextHolder에 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("사용자 인증 성공: " + username);
            
        } catch (ExpiredJwtException e) {
            log.error("AccessToken 이 만료되었습니다.", e);
            throw new AccessTokenExpiredException("AccessToken 이 만료되었습니다.");
        } catch (JwtException e) {
            log.error("Token 검증 실패", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("토큰이 유효성 검증에 실패했습니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
