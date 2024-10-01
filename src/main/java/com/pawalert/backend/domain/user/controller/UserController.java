package com.pawalert.backend.domain.user.controller;

import com.pawalert.backend.domain.user.model.JwtResponse;
import com.pawalert.backend.domain.user.model.LoginRequest;
import com.pawalert.backend.domain.user.model.RegisterRequest;
import com.pawalert.backend.domain.user.model.UserUpdateRequest;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.domain.user.service.UserService;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final UserRepository userRepository;

    @GetMapping("/checkemail")
    public ResponseEntity<SuccessResponse<HttpStatus>> checkEmailUser(@RequestParam("email") String email) {
        return userService.checkEmail(email);
    }

    // 내 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.getMyPage(customUserDetails);
    }

    // 내 정보 업데이트
    @PatchMapping("/update")
    public ResponseEntity<?> updateMyPage(@AuthenticationPrincipal CustomUserDetails user,
                                          @RequestBody UserUpdateRequest request
    ) {
        return userService.updateMyPage(request, user);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<String>> registerUser(@RequestBody RegisterRequest registerRequest) {

        return userService.registerUser(registerRequest);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    // 프로필 이미지 업데이트
    @PatchMapping("/updateProfileImage")
    public ResponseEntity<SuccessResponse<String>> updateProfileImage(@AuthenticationPrincipal CustomUserDetails user,
                                                                      @RequestPart("userImage") MultipartFile images) {
        return userService.updateProfileImage(user, images);
    }

    // 채팅상대방 ID 조회
    @GetMapping("/otherUser")
    private String otherUser(@AuthenticationPrincipal CustomUserDetails user,@RequestParam(value = "userUid") String otherUid) {

        String otherUidEmail = userRepository.findByUid(otherUid).get().getEmail();
        return otherUidEmail;
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
