package com.pawalert.backend.domain.user.service;

import com.pawalert.backend.domain.shelter.entity.AnimalRescueOrganizationEntity;
import com.pawalert.backend.domain.shelter.model.ShelterJoinDto;
import com.pawalert.backend.domain.shelter.repository.ShelterRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.model.RegisterRequest;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.LocataionRecord;
import com.pawalert.backend.global.aws.S3Service;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NonMemberSignupAndCertificationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShelterRepository shelterRepository;

    @Mock
    private UserValidationUtil userValidationUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private NonMemberSignupAndCertificationService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Test
    void login() {
    }


    @Test
    @DisplayName("회원가입-테스트")
    void registerUser() {
        // 준비 : 회원가입 요청 데이터
        RegisterRequest registerRequest = new RegisterRequest("test@email.com", "userName1", "password1234");
        //Mock 설정 : 비밀번호 인코딩
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(s3Service.basicProfile()).thenReturn("basic-ProfileImage");

        // 실행 : 사용자 등록
        ResponseEntity<SuccessResponse<String>> response = userService.registerUser(registerRequest);

        // 검증 : 응답 상태 코드와 메시지
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully!", Objects.requireNonNull(response.getBody()).message());
        assertTrue(response.getBody().data().contains("test@email.com"));

        // 사용자 저장이 한 번 호출되었는지 검증
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("비회원-보호센터 회원가입 테스트")
    void signupShelterInfo() {
        //준비: 비회원 보호센터 회원가입 요청 데이터
        LocataionRecord location = new LocataionRecord(
                new BigDecimal("37.5665"), new BigDecimal("126.978"),
                "100-101", "서울특별시 중구 세종대로 110",
                "국립도서관 옆"
        );

        ShelterJoinDto shelterJoinDto = new ShelterJoinDto(
                "test@email", "password1234",
                "관할구역:강남동", "마음보호센터",
                "031-1234-1234", location,
                "www.test.com", "마음보호센터@gmail.com"

        );
        // 실행 : 비회원 회원가입 등록
        ResponseEntity<SuccessResponse<String>> response = userService.signupShelterInfo(shelterJoinDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("비회원 보호센터 정보 등록 성공", Objects.requireNonNull(response.getBody()).message());

        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(shelterRepository, times(1)).save(any(AnimalRescueOrganizationEntity.class));

    }
}