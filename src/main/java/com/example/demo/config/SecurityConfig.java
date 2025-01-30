package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.DispatcherType;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (AJAX 요청 허용)
            .authorizeHttpRequests(authorize -> 
                authorize
                    .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // FORWARD 요청 허용
                    .requestMatchers(
                        "/css/**", "/js/**", "/images/**" // 정적 리소스는 누구나 접근 가능
                    ).permitAll()
                    .requestMatchers(
                        "/api/register", "/api/validateId", "/api/validateEmail", "/api/validatePhone" // ✅ AJAX 요청 허용
                    ).permitAll()
                    .requestMatchers(
                        "/login", "/register" // ✅ 로그인과 회원가입 페이지는 비인증 사용자만 접근 가능
                    ).anonymous() // ✅ 로그인되지 않은 사용자만 접근 가능
                    .anyRequest().authenticated() // 그 외 요청은 인증 필요
            )
            .formLogin(form -> form
                .loginPage("/login") // ✅ 로그인 페이지 URL
                .loginProcessingUrl("/doLogin") // 로그인 처리 URL
                .usernameParameter("memberId") // 사용자 ID 필드 이름
                .passwordParameter("memberPasswd") // 비밀번호 필드 이름
                .defaultSuccessUrl("/", true) // ✅ 로그인 성공 후 메인 페이지로 이동
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // 로그아웃 URL
                .logoutSuccessUrl("/") // ✅ 로그아웃 성공 후 메인 페이지로 이동
                .invalidateHttpSession(true) // 세션 삭제
                .clearAuthentication(true) // 인증 정보 삭제
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
