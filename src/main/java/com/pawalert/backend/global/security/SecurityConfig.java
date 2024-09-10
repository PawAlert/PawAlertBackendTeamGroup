package com.pawalert.backend.global.security;

import com.pawalert.backend.global.httpstatus.exception.CustomAuthenticationEntryPoint;
import com.pawalert.backend.global.jwt.JwtAuthenticationFilter;
import com.pawalert.backend.global.jwt.JwtTokenProvider;
import com.pawalert.backend.global.oauth.CustomOAuth2UserService;
import com.pawalert.backend.global.oauth.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          JwtTokenProvider jwtTokenProvider,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // JWT 인증을 사용할 경우 CSRF 보호 비활성화
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/",
                                        "/files/**",
                                        "/login",
                                        "/api/missing/search/**",
                                        "/swagger-ui/**",
                                        "/api/user/login",
                                        "/oauth2/**",
                                        "/api/hospital/doctor/certification",
                                        "/api/hospital/doctor/signupCreate",
                                        "/api/shelter/signupCreate",
                                        "/api/user/register",
                                        "/api/shelter/certification",
                                        "/api/missing/getdetail/**").permitAll() // 위 경로들은 모두 인증 없이 접근 가능
                                .anyRequest().authenticated() // 나머지 모든 요청은 인증이 필요함
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("https://pawalert.co.kr/login") // 사용자 정의 로그인 페이지 설정
                                .failureUrl("https://pawalert.co.kr/login?error=true") // 로그인 실패 시 리디렉션 URL 설정
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userService(customOAuth2UserService) // 커스텀 OAuth2UserService 사용
                                )
                                .successHandler(oAuth2AuthenticationSuccessHandler) // 인증 성공 시 사용자 정의 핸들러 사용
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않음, JWT로 상태 관리
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(customAuthenticationEntryPoint) // 인증 실패 시 사용자 정의 엔트리 포인트 사용
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
