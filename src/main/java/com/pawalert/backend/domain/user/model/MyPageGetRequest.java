package com.pawalert.backend.domain.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyPageGetRequest(
        @Schema(description = "사용자 UID", example = "user-1234")
        String uid,

        @Schema(description = "사용자 이메일", example = "user@example.com")
        String email,

        @Schema(description = "사용자 이름", example = "홍길동")
        String userName,

        @Schema(description = "사용자 전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "인증 제공자", example = "GOOGLE")
        String authProvider,

        @Schema(description = "프로필 이미지 URL", example = "http://example.com/profile.jpg")
        String profileImageUrl,

        @Schema(description = "사용자 역할", example = "USER")
        UserRole userRoles
) {
}
