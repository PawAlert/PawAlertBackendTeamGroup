package com.pawalert.backend.domain.inquiry.dto;

import com.pawalert.backend.domain.inquiry.entity.InquiryEntity;
import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;

@Builder
@Schema(description = "문의하기 DTO")
public record InquiryDto(
        Long id,
        @Schema(description = "문의 종류", example = "POST_RELATED")
        InquiryEntity.InquiryType type,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "이메일 주소", example = "hong@example.com")
        String email,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "문의 내용", example = "게시글 관련 문의 드립니다.")
        String content
) {
    public InquiryEntity toEntity(String userUid) {
        return InquiryEntity.builder()
                .id(id)
                .userUid(userUid)
                .type(type)
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .content(content)
                .build();
    }
}