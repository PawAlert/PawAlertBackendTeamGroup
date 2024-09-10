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

        // OAuth2 인증된 사용자 정보 가져오기
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String authorizationCode = request.getParameter("code"); // 인증 코드 가져오기

        // Google 토큰 엔드포인트 URL 구성
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        // 액세스 토큰 요청을 위한 파라미터 구성
        Map<String, String> tokenRequestParams = new HashMap<>();
        tokenRequestParams.put("code", authorizationCode);
        tokenRequestParams.put("client_id", "YOUR_CLIENT_ID");
        tokenRequestParams.put("client_secret", "YOUR_CLIENT_SECRET");
        tokenRequestParams.put("redirect_uri", "https://pawalert.co.kr/login/oauth2/code/google");
        tokenRequestParams.put("grant_type", "authorization_code");

        // 액세스 토큰 요청
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, tokenRequestParams, Map.class);
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 사용자 정보 요청
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v2/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                userInfoEndpoint, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        Map<String, Object> userInfo = userInfoResponse.getBody();
        String email = (String) userInfo.get("email");

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.generateToken(email);

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