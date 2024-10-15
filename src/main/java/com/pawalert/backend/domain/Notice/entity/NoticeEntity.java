package com.pawalert.backend.domain.Notice.entity;

import com.pawalert.backend.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "공지사항 엔티티")
public class NoticeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "공지사항 ID")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "공지사항 제목")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Schema(description = "공지사항 내용")
    private String content;

    @Column(nullable = false)
    @Schema(description = "작성자 ID")
    private String authorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "공지사항 상태")
    private NoticeStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "공지사항 유형")
    private NoticeType type;

    @Schema(description = "공지사항 유효 시작일")
    private LocalDateTime validFrom;

    @Schema(description = "공지사항 유효 종료일")
    private LocalDateTime validUntil;

    @Enumerated(EnumType.STRING)
    @Schema(description = "공지사항 중요도")
    private NoticePriority priority;

    @ElementCollection
    @Schema(description = "첨부 파일 URL 목록")
    private List<String> attachmentUrls = new ArrayList<>();


    @Schema(description = "공지사항 상태")
    public enum NoticeStatus {
        ACTIVE, INACTIVE
    }

    @Schema(description = "공지사항 유형")
    public enum NoticeType {
        GENERAL, URGENT, EVENT, MAINTENANCE
    }

    @Schema(description = "공지사항 중요도")
    public enum NoticePriority {
        HIGH, MEDIUM, LOW
    }
}