package com.pawalert.backend.domain.user.controller;

import com.pawalert.backend.domain.user.model.JwtResponse;
import com.pawalert.backend.domain.user.model.LoginRequest;
import com.pawalert.backend.domain.user.model.RegisterRequest;
import com.pawalert.backend.domain.user.model.UserUpdateRequest;
import com.pawalert.backend.domain.user.service.UserService;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(userService.getMyPage(customUserDetails));
    }

    // 마이페이지 업데이트
    @PatchMapping("/updateMyPage")
    public void updateMyPage(@AuthenticationPrincipal CustomUserDetails user,
                             @RequestPart("userUpdateDto") UserUpdateRequest request,
                             @RequestPart("userImage") MultipartFile images) {
        userService.updateMyPage(request, user, images);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {

        return userService.registerUser(registerRequest);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    // todo : 프론트단이 완성되면 추가하기로!
//    // 소셜 로그아웃
//    @PostMapping("/logout/social")
//    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
//        request.getSession().invalidate();  // 세션 무효화
//        return ResponseEntity.ok("Logged out successfully.");
//    }
//    //jwt
//    // 클라이언트 측에서 JWT를 제거하도록 안내하는 방법
//    // 클라이언트가 브라우저 쿠키나 로컬 스토리지에서 토큰을 삭제
}
