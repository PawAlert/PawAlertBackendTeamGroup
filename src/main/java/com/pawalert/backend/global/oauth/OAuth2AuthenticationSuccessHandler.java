package com.pawalert.backend.global.oauth;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawalert.backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate(); // HTTP 요청을 위한 RestTemplate

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        System.out.println("OAuth2AuthenticationSuccessHandler: onAuthenticationSuccess called");

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.generateToken(authentication.getName());


        // 응답의 Content-Type을 JSON으로 설정
        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());

        // 응답 바디에 JWT 토큰 포함
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", jwtToken);

        // JSON 응답 작성
        objectMapper.writeValue(response.getWriter(), responseBody);
    }
}