package com.pawalert.backend.domain.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterRequest(
        @Schema(description = "이메일", example = "example@email.com")
        String email,

        @Schema(description = "사용자 이름", example = "john_doe")
        String userName,

        @Schema(description = "비밀번호", example = "securePassword123")
        String password
) {
}
