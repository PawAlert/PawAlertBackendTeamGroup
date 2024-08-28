package com.pawalert.backend.global.oauth;

import com.pawalert.backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtTokenProvider.generateToken(userDetails.getUsername());

        // 쿠키에 JWT 토큰을 설정합니다.
        response.addCookie(createTokenCookie(jwtToken));

        // 성공 시 리디렉션할 URL을 설정합니다.
        String redirectUrl = "https://web-pawalertfrontteam-m06zwfj8628a2164.sel4.cloudtype.app/home";

        // 클라이언트 측에 리디렉션합니다.
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true); // 클라이언트 측 스크립트에서 접근하지 못하도록 설정
        cookie.setSecure(true); // HTTPS를 통해서만 전송되도록 설정
        cookie.setPath("/"); // 전체 경로에서 쿠키 접근 가능
        return cookie;
    }
}