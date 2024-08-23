package com.pawalert.backend.global;

import com.pawalert.backend.domain.user.entity.UserEntity;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SaveImage {

    @Value("${file.base-url}")
    private String baseUrl;

    @Value("${file.profile-upload-dir}")
    private String profileUploadDir;

    public @Size(max = 255) String saveProfileImage(UserEntity user) {
        // 기본 이미지 URL 반환
        return baseUrl + profileUploadDir;
    }
}
