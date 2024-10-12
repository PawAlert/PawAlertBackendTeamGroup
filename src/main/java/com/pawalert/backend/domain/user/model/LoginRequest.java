package com.pawalert.backend.domain.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "이메일", example = "admin@admin.com")
        String email,

        @Schema(description = "비밀번호", example = "12341234")
        String password
) {
}
