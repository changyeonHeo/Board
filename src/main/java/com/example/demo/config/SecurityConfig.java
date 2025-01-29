package com.example.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (AJAX 요청 허용)
            .authorizeHttpRequests(authorize -> 
                authorize
                    .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // FORWARD 요청 허용
                    .requestMatchers(
                        "/", "/login", "/register", "/css/**", "/js/**", "/images/**" // 정적 리소스 및 기본 경로 허용
                    ).permitAll()
                    .requestMatchers(
                        "/api/validateId", "/api/validateEmail", "/api/validatePhone" // ✅ AJAX 요청 허용
                    ).permitAll()
                    .anyRequest().authenticated() // 그 외 요청은 인증 필요
            )
            .formLogin(form -> form
                .loginPage("/login") // ✅ 로그인 페이지 URL 변경 (기본 `/login`)
                .loginProcessingUrl("/process-login") // 로그인 처리 URL
                .usernameParameter("memberId") // 사용자 ID 필드 이름
                .passwordParameter("memberPasswd") // 비밀번호 필드 이름
                .defaultSuccessUrl("/", true) // 로그인 성공 시 메인 페이지로 이동
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // 로그아웃 URL
                .logoutSuccessUrl("/") // 로그아웃 성공 시 메인 페이지로 이동
                .permitAll()
            )
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/403") // 권한 없는 페이지 접근 시 이동할 경로
            );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화 설정
    }
}
