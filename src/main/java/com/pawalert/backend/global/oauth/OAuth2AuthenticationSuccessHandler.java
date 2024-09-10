package com.pawalert.backend.global.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawalert.backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // DefaultOAuth2User로 캐스팅
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        // 사용자 이름(email)을 가져옵니다.
        String username = oAuth2User.getAttribute("email");

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.generateToken(username);

        response.addHeader("Authorization", jwtToken);


        String targetUrl = UriComponentsBuilder.fromUriString("https://pawalert.co.kr/")
                .queryParam("token", jwtToken)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
        response.sendRedirect(targetUrl);

    }
}