package com.pawalert.backend.domain.user.service;

import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.model.*;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.SaveImage;
import com.pawalert.backend.global.config.AsyncService;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import com.pawalert.backend.global.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AsyncService asyncService;
    private final SaveImage saveImage;

    // 이미 존재하는 이메일 체크
    public ResponseEntity<?> checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
        return ResponseHandler.ok("사용 가능한 이메일입니다.", HttpStatus.OK);
    }

    // 회원가입
    @Transactional
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {

        try {
            UserEntity user = UserEntity.builder()
                    .email(registerRequest.email())
                    .password(passwordEncoder.encode(registerRequest.password()))
                    .userName(registerRequest.username())
                    .role(UserRole.ROLE_USER)
                    .uid(UUID.randomUUID().toString())
                    .authProvider("localUser")
                    .build();
            userRepository.save(user);
            System.out.println("처리 완료");
            CompletableFuture.runAsync(() -> {
                try {
                    // 프로필 이미지 저장
                    System.out.println("이미지 저장 시작");
                    asyncService.saveProfileImage(user);
                } catch (Exception e) {
                    log.error("프로필 이미지 저장 중 오류 발생", e);
                }
            });

            return ResponseHandler.generateResponse(HttpStatus.CREATED, "User registered successfully!", "사용자 이메일 : " + user.getEmail());

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 로그인
    public ResponseEntity<?> login(LoginRequest loginRequest) {

        try {
            UserEntity user = userRepository.findByEmail(loginRequest.email())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUid(),
                            loginRequest.password()
                    )
            );

            String uid = ((CustomUserDetails) authentication.getPrincipal()).getUid(); // uid 가져오기
            String jwt = jwtTokenProvider.generateToken(uid); //uid 기반으로

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
            throw new BusinessException(ErrorCode.INVALID_LOGIN_CREDENTIALS);
        }
    }

    // 마이페이지 수정
    public ResponseEntity<?> updateMyPage(UserUpdateRequest request, CustomUserDetails user) {
        // 사용자 정보 조회
        UserEntity userEntity = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        try {
            // 사용자 정보 업데이트
            userEntity.setUserName(request.username());
            userEntity.setPhoneNumber(request.phoneNumber());
            // 사용자 정보 저장
            userRepository.save(userEntity);
            // 성공 응답 반환
            return ResponseHandler.generateResponse(HttpStatus.OK, "내 정보 수정 성공", "사용자 이메일 : " + userEntity.getEmail());

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.FAIL_MY_UPDATE, "마이페이지 수정 중 오류가 발생했습니다.");
        }
    }


    // myPage 조회
    public ResponseEntity<SuccessResponse<MyPageGetRequest>> getMyPage(CustomUserDetails user) {


        UserEntity memberUser = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        // MyPage 응답 생성
        MyPageGetRequest response = new MyPageGetRequest(
                memberUser.getUid(),
                memberUser.getEmail(),
                memberUser.getUserName(),
                memberUser.getPhoneNumber(),
                memberUser.getAuthProvider(),
                memberUser.getProfilePictureUrl(),
                memberUser.getRole()
        );

        // 성공 응답 반환
        return ResponseHandler.generateResponse(HttpStatus.OK, "MyPage 조회 성공", response);
    }

    // 프로필 이미지 업데이트
    public ResponseEntity<?> updateProfileImage(CustomUserDetails user, MultipartFile images) {
        // 사용자 정보 조회
        UserEntity userEntity = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        try {
            // 이미지가 있는 경우 프로필 이미지 업데이트
            if (images != null && !images.isEmpty()) {
                userEntity.setProfilePictureUrl(saveImage.SaveImages(images));
            }

            // 사용자 정보 저장
            userRepository.save(userEntity);

            // 성공 응답 반환
            return ResponseHandler.generateResponse(HttpStatus.OK, "프로필 이미지 업데이트 성공", "사용자 이메일 : " + userEntity.getEmail());

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NOT_FOUND_MEMBER, "프로필 이미지 업데이트 중 오류가 발생했습니다.");
        }
    }

}
