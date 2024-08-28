package com.pawalert.backend.global.oauth;

import com.pawalert.backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.generateToken(authentication.getName());

        // 응답 JSON 생성
        String jsonResponse = "{"
                + "\"status\": \"OK\","
                + "\"message\": \"Login successful\","
                + "\"data\": {"
                + "\"token\": \"" + jwtToken + "\""
                + "}"
                + "}";

        // 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 응답을 클라이언트로 작성
        response.getWriter().write(jsonResponse);
    }
}
