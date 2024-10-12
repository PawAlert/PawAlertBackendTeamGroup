package com.pawalert.backend.domain.user.controller;

import com.pawalert.backend.domain.shelter.model.ShelterJoinDto;
import com.pawalert.backend.domain.user.model.JwtResponse;
import com.pawalert.backend.domain.user.model.LoginRequest;
import com.pawalert.backend.domain.user.model.RegisterRequest;
import com.pawalert.backend.domain.user.service.NonMemberSignupAndCertificationService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/nonMember/user")
@Tag(name = "로그인, 회원가입 등등 비회원 관련 API 입니다", description = "비회원 API")
public class NonMemberSignupAndCertificationController {

    private final NonMemberSignupAndCertificationService nonLoginMemberUserService;


    // 이메일 체크
    @GetMapping("/checkemail")
    @Operation(summary = "이메일 중복 체크", description = "사용중인 이메일을 확인합니다.")
    public ResponseEntity<SuccessResponse<HttpStatus>> checkEmailUser(@RequestParam("email") String email) {
        return nonLoginMemberUserService.checkEmail(email);
    }

    // 회원가입
    @PostMapping("/register")
    @Operation(summary = "회원가입 API", description = "회원가입 API 입니다.")
    public ResponseEntity<SuccessResponse<String>> registerUser(@RequestBody RegisterRequest registerRequest) {

        return nonLoginMemberUserService.registerUser(registerRequest);
    }
//    SuccessResponse<JwtResponse>
    // 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "로그인 API 입니다.")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return nonLoginMemberUserService.login(loginRequest);
    }

    // 비회원 보호센터 정보 등록
    @PostMapping(value = "/signupCreate")
    @Operation(summary = "비회원의 경우 보호센터 정보를 등록합니다.", description = "비회원 보호센터 회원가입 API 입니다.")
    public ResponseEntity<SuccessResponse<String>> createHospitalDoctor(@RequestBody ShelterJoinDto request
    ) {
        return nonLoginMemberUserService.signupShelterInfo(request);
    }
}
