package com.pawalert.backend.global.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawalert.backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 디버깅을 위한 로그 추가
        System.out.println("OAuth2AuthenticationSuccessHandler: onAuthenticationSuccess called");

        // 인증된 사용자 정보 가져오기
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String username = oAuth2User.getAttribute("email");

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.generateToken(username);

        // 응답의 Content-Type을 JSON으로 설정
        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());

        // 응답 바디에 JWT 토큰 포함
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", jwtToken);
        responseBody.put("test", "test");

        // JSON 응답 작성
        objectMapper.writeValue(response.getWriter(), responseBody);
    }
}
