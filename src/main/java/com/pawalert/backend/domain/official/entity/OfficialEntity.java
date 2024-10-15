package com.pawalert.backend.domain.official.entity;

import com.pawalert.backend.global.BaseEntity;
import com.pawalert.backend.global.Location;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfficialEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "official_id")
    @Schema(description = "기관 고유 ID", example = "1")
    private Long id;

    @Column(name = "user_uid")
    private String userUid;

    @Column(name = "institution_name")
    @Schema(description = "기관명", example = "서울동물보호센터")
    private String institutionName;

    @Column(name = "representative_name")
    @Schema(description = "대표자명", example = "홍길동")
    private String representativeName;

    @Column(name = "email")
    @Schema(description = "이메일 주소", example = "example@pawalert.com")
    private String email;

    @Column(name = "phone_number")
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Column(name = "institution_type")
    @Schema(description = "기관 유형", example = "동물병원")
    private String institutionType;

    @Embedded
    @Schema(description = "기관 위치")
    private Location location;

    @Column(name = "website")
    @Schema(description = "웹사이트 URL", example = "https://example.com")
    private String website;

    @Column(name = "institution_description")
    @Schema(description = "기관 설명", example = "서울동물보호센터는 유기동물 구조 및 보호 활동을 진행하는 기관입니다.")
    private String institutionDescription;

    @Column(name = "operating_hours")
    @Schema(description = "운영 시간", example = "09:00 ~ 18:00")
    private String operatingHours;

    @Column(name = "registration_document")
    @Schema(description = "사업자등록증 및 기관인증 서류 번호", example = "123456")
    private String registrationNumber;

    @ElementCollection
    @CollectionTable(name = "official_images", joinColumns = @JoinColumn(name = "official_id"))
    @Column(name = "image_url")
    @Schema(description = "추가 이미지 URL 목록")
    private List<String> additionalImages;

    @Column(name = "terms_agreed")
    @Schema(description = "이용 약관 동의 여부", example = "true")
    private boolean termsAgreed;

    @Column(name = "privacy_policy_agreed")
    @Schema(description = "개인정보 처리방침 동의 여부", example = "true")
    private boolean privacyPolicyAgreed;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Schema(description = "승인 상태", example = "PENDING")
    private ApprovalStatus status;


    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED
    }
}