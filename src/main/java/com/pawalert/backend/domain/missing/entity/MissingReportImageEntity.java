package com.pawalert.backend.domain.missing.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissingReportImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "missing_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missing_report_id", nullable = false)
    private MissingReportEntity missingReport;

    @Size(max = 255)
    @Column(name = "missing_photo_url")
    @Schema(description = "사진 URL")
    private String missingPhotoUrl;

    @Column(name = "is_deleted")
    @Schema(description = "이미지 삭제 여부")
    private boolean isDeleted;
}
