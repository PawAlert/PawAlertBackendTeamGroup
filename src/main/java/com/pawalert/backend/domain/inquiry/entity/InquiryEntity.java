package com.pawalert.backend.domain.inquiry.entity;

import com.pawalert.backend.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "문의 엔티티")
public class InquiryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "문의 ID")
    private Long id;

    @Schema(description = "사용자 Uid")
    private String userUid;

    @Enumerated(EnumType.STRING)
    @Schema(description = "문의 종류")
    private InquiryType type;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "이메일 주소")
    private String email;

    @Schema(description = "전화번호")
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "문의 내용")
    private String content;

    @Schema(description = "문의 종류")
    public enum InquiryType {
        @Schema(description = "글 관련")
        POST_RELATED,
        @Schema(description = "신고")
        REPORT,
        @Schema(description = "기타")
        OTHER
    }
}