package com.pawalert.backend.domain.user.service;

import com.pawalert.backend.domain.shelter.entity.AnimalRescueOrganizationEntity;
import com.pawalert.backend.domain.shelter.entity.AnimalShelterEntity;
import com.pawalert.backend.domain.shelter.model.ShelterUpdateOrCreateRequest;
import com.pawalert.backend.domain.shelter.repository.AnimalShelterRepository;
import com.pawalert.backend.domain.shelter.repository.ShelterRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.model.JwtResponse;
import com.pawalert.backend.domain.user.model.LoginRequest;
import com.pawalert.backend.domain.user.model.RegisterRequest;
import com.pawalert.backend.domain.user.model.UserRole;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.ImageInfo;
import com.pawalert.backend.global.Location;
import com.pawalert.backend.global.aws.S3Service;
import com.pawalert.backend.global.aws.SaveImage;
import com.pawalert.backend.global.config.redis.RedisService;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import com.pawalert.backend.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class NonMemberSignupAndCertificationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    // 유저 검증 유틸
    private final UserValidationUtil userValidationUtil;

    // ---
    private final ShelterRepository shelterRepository;
    private final SaveImage saveImage;
    private final AnimalShelterRepository animalShelterRepository;
    private final RedisService redisService;

    // 이미 존재하는 이메일 체크
    public ResponseEntity<SuccessResponse<HttpStatus>> checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
        return ResponseHandler.ok("사용 가능한 이메일입니다.", HttpStatus.OK);
    }

    // 일반유저 회원가입
    @Transactional
    public ResponseEntity<SuccessResponse<String>> registerUser(RegisterRequest registerRequest) {
        try {
            UserEntity user = UserEntity.builder()
                    .email(registerRequest.email())
                    .password(passwordEncoder.encode(registerRequest.password()))
                    .userName(registerRequest.userName())
                    .role(UserRole.ROLE_USER)
                    .uid(UUID.randomUUID().toString())
                    // 기본이미지 적용
                    .profilePictureUrl(s3Service.basicProfile())
                    .authProvider("localUser")
                    .build();
            userRepository.save(user);


            return ResponseHandler.generateResponse(HttpStatus.CREATED, "User registered successfully!", "사용자 이메일 : " + user.getEmail());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    // 로그인
    public ResponseEntity<SuccessResponse<JwtResponse>> login(LoginRequest loginRequest) {

        UserEntity userInfo = userValidationUtil.validateUserEmail(loginRequest.email());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userInfo.getUid(),
                        loginRequest.password()
                )
        );

        String uid = ((CustomUserDetails) authentication.getPrincipal()).getUid();
        String jwt = jwtTokenProvider.generateToken(uid);


        // 성공적인 응답 생성
        SuccessResponse<JwtResponse> response = new SuccessResponse<>(
                HttpStatus.OK,
                "Login successful",
                new JwtResponse(jwt)
        );


        // JWT 토큰을 헤더에 추가하고 성공 응답 반환
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt)
                .body(response);

    }

}
