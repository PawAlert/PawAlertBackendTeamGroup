package com.pawalert.backend.domain.official.model;

import com.pawalert.backend.domain.official.entity.OfficialEntity;
import com.pawalert.backend.global.Location;
import com.pawalert.backend.global.LocationRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "기관 등록 DTO")
public record OfficialRegistrationDto(
        @Schema(description = "기관명", example = "서울동물보호센터")
        String institutionName,

        @Schema(description = "대표자명", example = "홍길동")
        String representativeName,

        @Schema(description = "이메일 주소", example = "seoul_animal@example.com")
        String email,

        @Schema(description = "전화번호", example = "02-1234-5678")
        String phoneNumber,

        @Schema(description = "기관 유형", example = "동물보호센터")
        String institutionType,

        @Schema(description = "기관 위치")
        LocationRecord location,

        @Schema(description = "웹사이트 URL", example = "https://www.seoulanimalcenter.com")
        String website,

        @Schema(description = "기관 설명", example = "서울시에서 운영하는 동물보호센터로, 유기동물 보호 및 입양 서비스를 제공합니다.")
        String institutionDescription,

        @Schema(description = "운영 시간", example = "평일 09:00-18:00, 주말 10:00-17:00")
        String operatingHours,

        @Schema(description = "사업자등록증 및 기관인증 서류 번호", example = "123-45-67890")
        String registrationNumber,

        @Schema(description = "추가 이미지 URL 목록", example = "[\"https://example.com/images/center1.jpg\", \"https://example.com/images/center2.jpg\"]")
        List<String> additionalImages,

        @Schema(description = "이용 약관 동의 여부", example = "true")
        boolean termsAgreed,

        @Schema(description = "개인정보 처리방침 동의 여부", example = "true")
        boolean privacyPolicyAgreed
) {
    public OfficialEntity toEntity(String userUid, OfficialEntity.ApprovalStatus status, Location location) {
        return OfficialEntity.builder()
                .userUid(userUid)
                .institutionName(institutionName)
                .representativeName(representativeName)
                .email(email)
                .phoneNumber(phoneNumber)
                .institutionType(institutionType)
                .website(website)
                .location(location)
                .institutionDescription(institutionDescription)
                .operatingHours(operatingHours)
                .registrationNumber(registrationNumber)
                .additionalImages(additionalImages)
                .termsAgreed(termsAgreed)
                .privacyPolicyAgreed(privacyPolicyAgreed)
                .status(status)
                .build();
    }
}