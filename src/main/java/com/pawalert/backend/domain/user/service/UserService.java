package com.pawalert.backend.domain.user.service;

import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.model.*;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.SaveImage;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import com.pawalert.backend.global.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final SaveImage saveImage;

    // 회원가입
    @Transactional
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        // 이미 존재하는 이메일인지 확인
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
        try {
            UserEntity user = UserEntity.builder()
                    .email(registerRequest.email())
                    .password(passwordEncoder.encode(registerRequest.password()))
                    .userName(registerRequest.username())
                    .role(UserRole.ROLE_USER)
                    .uid(UUID.randomUUID().toString())
                    .authProvider("localUser")
                    .build();
            user.setProfilePictureUrl(saveImage.saveProfileImage(user));
            userRepository.save(user);
            return ResponseHandler.generateResponse(HttpStatus.CREATED, "User registered successfully!", "사용자 이메일 : " + user.getEmail());

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 로그인
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            String email = ((CustomUserDetails) authentication.getPrincipal()).getUsername(); // email 가져오기
            String jwt = jwtTokenProvider.generateToken(email); //email 기반으로

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

        } catch (BadCredentialsException e) {
            throw new  BusinessException(ErrorCode.INVALID_LOGIN_CREDENTIALS);
        }
    }

    // 마이페이지 수정
    public void updateMyPage(UserUpdateRequest request, CustomUserDetails user, MultipartFile images) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        if (images != null) {
            userEntity.setProfilePictureUrl(saveImage.SaveImages(images));
        }

        userEntity.setUserName(request.username());
        userEntity.setPhoneNumber(request.phoneNumber());
        userEntity.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(userEntity);
    }

    // myPage 조회
    public MyPageGetRequest getMyPage(CustomUserDetails user) {
        UserEntity memberUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        return new MyPageGetRequest(
                memberUser.getUid(),
                memberUser.getEmail(),
                memberUser.getUserName(),
                memberUser.getPhoneNumber(),
                memberUser.getAuthProvider(),
                memberUser.getProfilePictureUrl(),
                memberUser.getRole()
        );
    }
}
