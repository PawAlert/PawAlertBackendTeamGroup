package com.pawalert.backend.domain.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserUpdateRequest(
        @Schema(description = "사용자 이름", example = "john_doe")
        String username, // 사용자 이름

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber // 전화번호
) {
}
