package com.pawalert.backend.domain.user.controller;

import com.pawalert.backend.domain.shelter.model.ShelterJoinDto;
import com.pawalert.backend.domain.user.model.JwtResponse;
import com.pawalert.backend.domain.user.model.LoginRequest;
import com.pawalert.backend.domain.user.model.RegisterRequest;
import com.pawalert.backend.domain.user.service.NonMemberSignupAndCertificationService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/user")

public class NonMemberSignupAndCertificationController {

    private final NonMemberSignupAndCertificationService nonLoginMemberUserService;


    // 이메일 체크
    @GetMapping("/checkemail")
    public ResponseEntity<SuccessResponse<HttpStatus>> checkEmailUser(@RequestParam("email") String email) {
        return nonLoginMemberUserService.checkEmail(email);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<String>> registerUser(@RequestBody RegisterRequest registerRequest) {

        return nonLoginMemberUserService.registerUser(registerRequest);
    }

    // 로그인
    @GetMapping("/login")
    public ResponseEntity<SuccessResponse<JwtResponse>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return nonLoginMemberUserService.login(loginRequest);
    }

    // 비회원 보호센터 정보 등록
    @PostMapping(value = "/signupCreate")
    public ResponseEntity<SuccessResponse<String>> createHospitalDoctor(@RequestBody ShelterJoinDto request
    ) {
        return nonLoginMemberUserService.signupShelterInfo(request);
    }
}
