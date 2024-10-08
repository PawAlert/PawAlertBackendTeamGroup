package com.pawalert.backend.global.oauth;

import com.pawalert.backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
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

    //
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, IOException {
//        // JWT 토큰 생성
//        String jwtToken = jwtTokenProvider.generateToken(authentication.getName());
//
//
//        // JWT 토큰을 헤더로 설정
//        response.setHeader("Authorization", "Bearer " + jwtToken); // "Authorization" 헤더에 JWT 토큰 설정
//
//        // 리다이렉트 URL 설정
//        String targetUrl = "https://pawalert.co.kr"; // 리다이렉트할 URL 설정
//
//        // 리다이렉트 처리
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
//    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.generateToken(authentication.getName());

        // 리다이렉트 URL 설정 (JWT 토큰을 쿼리 파라미터로 포함)
        String targetUrl = "https://pawalert.co.kr/?token=" + jwtToken; // 클라이언트가 받을 URL

        // 리다이렉트 처리
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}