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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, IOException {
        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.generateToken(authentication.getName());

        // JWT 토큰을 쿠키로 설정
        Cookie jwtCookie = new Cookie("jwtToken", jwtToken);
        jwtCookie.setHttpOnly(true); // 클라이언트에서 자바스크립트로 접근 불가하게 설정
        jwtCookie.setSecure(true); // HTTPS 사용 시에만 전송되도록 설정 (보안 강화)
        jwtCookie.setPath("/"); // 쿠키가 모든 경로에서 사용되도록 설정
        jwtCookie.setMaxAge(60 * 60); // 쿠키 유효기간을 1시간으로 설정

        // 쿠키를 응답에 추가
        response.addCookie(jwtCookie);

        // 리다이렉트 URL 설정
        String targetUrl = determineTargetUrl(request, response);

        // 리다이렉트 처리
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}