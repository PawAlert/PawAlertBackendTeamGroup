package com.pawalert.backend.domain.user.service;

import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("유저 uid-email 검증 단위테스트")
class UserValidationUtilTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidationUtil userValidationUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Test
    @DisplayName("uid 검증")
    void validateUserUid() {
        // 준비 : 유저 데이터를 Mock 으로 생성
        UserEntity mockUser = new UserEntity();
        mockUser.setUid("test-uid");
        when(userRepository.findByUid(anyString())).thenReturn(Optional.of(mockUser));
        // 실행
        UserEntity result = userValidationUtil.validateUserUid("test-uid");
        // 검증 : 반환된 유저가 예상한 값인지 확인
        assert (result.getUid().equals("test-uid"));
    }

    @Test
    @DisplayName("email 검증")
    void validateUserEmail() {
        // 준비 : 유저 데이터를 Mock 으로 설정
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail("test-email");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        // 실행
        UserEntity result = userValidationUtil.validateUserEmail(mockUser.getEmail());
        assert (result.getEmail().equals("test-email"));
    }

    @Test
    @DisplayName("유저 이메일 존재 bool")
    void validateUserEmailBool() {
        //준비
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail("test-email");
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        // 실행
        boolean result = userValidationUtil.existsByEmail(mockUser.getEmail());
        assert (result);
    }
}