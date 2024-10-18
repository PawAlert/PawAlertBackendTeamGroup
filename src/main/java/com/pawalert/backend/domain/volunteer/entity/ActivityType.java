package com.pawalert.backend.domain.volunteer.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "봉사활동 유형")
public enum ActivityType {
    @Schema(description = "돌봄 봉사")
    CARE,

    @Schema(description = "의료 지원")
    MEDICAL,

    @Schema(description = "입양 지원")
    ADOPTION,

    @Schema(description = "교육 및 훈련")
    EDUCATION,

    @Schema(description = "시설 봉사")
    FACILITY,

    @Schema(description = "행사 지원")
    EVENT,

    @Schema(description = "기타")
    OTHER
}