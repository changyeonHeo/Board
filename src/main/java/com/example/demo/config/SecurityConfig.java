package com.example.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화
            .authorizeHttpRequests(authorize -> 
                authorize
                    .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // FORWARD 요청 허용
                    .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll() // 인증 없이 접근 가능한 경로
                    .anyRequest().authenticated() // 나머지 요청은 인증 필요
            )
            .formLogin(form -> form
                .loginPage("/")  // 커스텀 첫 페이지 ("/" 경로)
                .loginProcessingUrl("/login")  // 로그인 처리 경로
                .usernameParameter("memberId") // 폼의 사용자 ID 필드 이름
                .passwordParameter("memberPasswd") // 폼의 비밀번호 필드 이름
                .defaultSuccessUrl("/", true)  // 로그인 성공 후 "/"로 리디렉션
            )
            .logout(logout -> logout
                .logoutUrl("/logout")  // 로그아웃 URL
                .logoutSuccessUrl("/")  // 로그아웃 성공 후 "/"로 리디렉션
                .permitAll()
            )
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/403") // 권한 없는 페이지 접근 시 이동할 경로
            );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // 정적 리소스 무시
    }
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화 설정
    }
}