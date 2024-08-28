package com.pawalert.backend.global.oauth;

import com.pawalert.backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
        // JWT 토큰을 쿠키로 설정
        Cookie jwtCookie = new Cookie("jwtToken", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60);
        response.addCookie(jwtCookie);
        // 성공 시 리디렉션할 URL을 설정합니다.
        String redirectUrl = "https://web-pawalertfrontteam-m06zwfj8628a2164.sel4.cloudtype.app/home";


        // 클라이언트 측에 리디렉션합니다.
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }


}