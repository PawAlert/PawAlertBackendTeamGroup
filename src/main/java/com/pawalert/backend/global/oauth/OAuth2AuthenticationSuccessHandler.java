package com.pawalert.backend.global.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawalert.backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper; // JSON 응답을 위한 ObjectMapper

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // DefaultOAuth2User로 캐스팅
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        // 사용자 이름(email)을 가져옵니다.
        String username = oAuth2User.getAttribute("email");

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.generateToken(username);

        // 응답 데이터를 생성
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("token", jwtToken);

        // 응답을 JSON 형식으로 변환하여 클라이언트에 전송
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));

    }
}