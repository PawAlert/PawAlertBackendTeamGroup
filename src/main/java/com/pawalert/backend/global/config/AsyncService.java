package com.pawalert.backend.global.config;

import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.global.SaveImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncService {
    private final SaveImage saveImage;

    @Async
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public CompletableFuture<String> saveProfileImage(UserEntity user) {
        try {
            // todo : s3로 바꿔주기
            user.setProfilePictureUrl(saveImage.saveProfileImage(user));

            return CompletableFuture.completedFuture("이미지 저장성공"); // 성공 시 이미지 URL 반환
        } catch (Exception e) {
            return CompletableFuture.completedFuture("이미지 저장 실패");
        }
    }
}
