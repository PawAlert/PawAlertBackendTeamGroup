package com.pawalert.backend.domain.user.controller;

import com.pawalert.backend.domain.user.model.JwtResponse;
import com.pawalert.backend.domain.user.model.LoginRequest;
import com.pawalert.backend.domain.user.model.RegisterRequest;
import com.pawalert.backend.domain.user.service.UserService;
import com.pawalert.backend.global.jwt.JwtTokenProvider;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Long userId = customUserDetails.getId();
        String username = customUserDetails.getUsername();
        log.info("User ID: " + userId + ", Username: " + username);
        String authProvider = customUserDetails.getAuthProvider();

        // 사용자 프로필 정보 반환
        return ResponseEntity.ok("User ID: " + userId + ", Username: " + username + ", Auth Provider: " + authProvider);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        return userService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        ResponseEntity<JwtResponse> responseEntity = userService.login(loginRequest);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String token = responseEntity.getBody().getToken();
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .body("Login successful");
        } else {
            return responseEntity;
        }
    }
}
