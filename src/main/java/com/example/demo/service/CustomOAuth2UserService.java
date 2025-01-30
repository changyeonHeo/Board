package com.example.demo.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // 카카오 로그인일 경우, 카카오에서 제공하는 user 정보를 가져옴
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        if (!"kakao".equals(registrationId)) {
            throw new IllegalArgumentException("지원하지 않는 로그인 서비스입니다.");
        }

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");

        return new CustomOAuth2User(nickname, email, attributes);
    }
}
