package com.pawalert.backend.domain.shelter.entity;

import com.pawalert.backend.global.BaseEntity;
import com.pawalert.backend.global.ImageInfo;
import com.pawalert.backend.global.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_shelter_name", columnList = "shelter_name")
})
public class AnimalRescueOrganizationEntity extends BaseEntity {
    @Id
    @Column(name = "organization_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "shelter_name", nullable = false)
    @Schema(description = "보호 센터 명")
    private String shelterName;

    @Size(max = 255)
    @NotNull
    @Column(name = "jurisdiction", nullable = false)
    @Schema(description = "관할 구역")
    private String jurisdiction;

    @Size(max = 20)
    @Column(name = "contact_phone", length = 20)
    @Schema(description = "동물보호센터 연락번호")
    private String contactPhone;

    @Size(max = 255)
    @NotNull
    @Column(name = "contact_email", nullable = false)
    @Schema(description = "연락 이메일")
    private String contactEmail;

    @Size(max = 255)
    @Column(name = "website_url")
    @Schema(description = "웹사이트 URL")
    private String websiteUrl;

    @Schema(description = "사용자 ID")
    @Column(name = "user_id")
    private Long userId;

    @Embedded
    @Schema(description = "보호단체 이미지 정보")
    private ImageInfo profileImage;

    @Embedded
    @Schema(description = "실종 위치 (위도/경도) 및 상세주소")
    private Location detailAddress;


}
