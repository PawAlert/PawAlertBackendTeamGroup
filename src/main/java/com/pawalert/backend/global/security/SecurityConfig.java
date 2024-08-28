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
                                        "/login",
                                        "/api/missing/search/**",
                                        "/swagger-ui/**",
                                        "/api/user/login",
                                        "/oauth2/**",
                                        "/api/user/register",
                                        "/api/missing/getdetail/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("https://web-pawalertfrontteam-m06zwfj8628a2164.sel4.cloudtype.app/home")
                                .failureUrl("https://web-pawalertfrontteam-m06zwfj8628a2164.sel4.cloudtype.app/login?error=true")
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .userService(customOAuth2UserService)
                                )
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용하지 않음
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(customAuthenticationEntryPoint) // 변경된 부분
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
