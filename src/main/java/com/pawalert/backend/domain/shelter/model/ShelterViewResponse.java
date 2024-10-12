package com.pawalert.backend.domain.shelter.model;

import com.pawalert.backend.global.LocataionRecord;
import io.swagger.v3.oas.annotations.media.Schema;

public record ShelterViewResponse(
        @Schema(description = "보호센터 ID", example = "1")
        Long shelterId,

        @Schema(description = "관할구역", example = "서울특별시")
        String jurisdiction,

        @Schema(description = "보호센터명", example = "서울 동물 보호소")
        String shelterName,

        @Schema(description = "보호센터 연락번호", example = "010-1234-5678")
        String contactPhone,

        @Schema(description = "보호센터 위치 (위도/경도) 및 상세주소")
        LocataionRecord location,

        @Schema(description = "보호센터 웹사이트 URL", example = "http://example.com/shelter")
        String shelterImage,

        @Schema(description = "보호센터 이메일", example = "contact@example.com")
        String shelterEmail,

        @Schema(description = "사용자 ID", example = "123")
        Long userId
) {
}
