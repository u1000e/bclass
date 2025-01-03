package com.kh.bclass.configuration;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, StreamWriteException, DatabindException, java.io.IOException {
        log.error("Authentication error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("type", "about:blank");
        body.put("title", "Unauthorized");
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("detail", authException.getMessage());
        body.put("instance", request.getRequestURI());

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
