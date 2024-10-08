package com.pawalert.backend.domain.user.service;

import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidationUtil {

    private final UserRepository userRepository;

    // 유저 UID로 조회
    public UserEntity validateUserUid(String uid) {
        return userRepository.findByUid(uid)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));
    }

    // 유저 Email 로 조회
    public UserEntity validateUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));
    }

    // 유저 email 존재유무 확인
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
