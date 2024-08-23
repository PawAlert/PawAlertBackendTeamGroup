package com.pawalert.backend.domain.user.controller;

import com.pawalert.backend.global.jwt.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getId();
        String username = customUserDetails.getUsername();
        String authProvider = customUserDetails.getAuthProvider();

        // 사용자 프로필 정보 반환
        return "User ID: " + userId + ", Username: " + username + ", Auth Provider: " + authProvider;
    }
}