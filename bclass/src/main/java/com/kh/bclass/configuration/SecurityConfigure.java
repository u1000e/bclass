package com.kh.bclass.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.kh.bclass.configuration.jwt.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfigure {
	
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final JwtFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP베이직안씀
                .csrf(AbstractHttpConfigurer::disable)	   // csrf 필요없어
                .cors(Customizer.withDefaults())	  // cors react서버로 받기
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/member/login", "/member/join", "/member/refresh", "/member/logout").permitAll(); // 얘는 권한없이도 오키
                    requests.requestMatchers(HttpMethod.GET, "/boards/**").permitAll(); // board관련 GET은 다 받아주장
                    requests.requestMatchers(HttpMethod.POST, "/api/**").authenticated(); // 요렇게 오는애들은 모두 토큰 필요
                    requests.requestMatchers(HttpMethod.POST, "/boards").hasRole("USER"); // ROLE_USER 권한 있는 애들만 boards(POST)요청가능
                })
                .exceptionHandling(exception ->
                		exception.authenticationEntryPoint(customAuthenticationEntryPoint))
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션은 안쓸거 ㅋ
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class) // JWT필터를 UsernamePasswordAuthenticationFilter 앞에 붙임
                .build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 프론트엔드 주소
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // 필요 시 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

}
