package com.pawalert.backend.domain.Notice.model;

import com.pawalert.backend.domain.Notice.entity.NoticeEntity;
import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "공지사항 DTO")
public record NoticeDto(
        @Schema(description = "공지사항 제목")
        String title,

        @Schema(description = "공지사항 내용")
        String content,

        @Schema(description = "공지사항 유형")
        NoticeEntity.NoticeType type,

        @Schema(description = "공지사항 유효 시작일")
        LocalDateTime validFrom,

        @Schema(description = "공지사항 유효 종료일")
        LocalDateTime validUntil,

        @Schema(description = "공지사항 중요도")
        NoticeEntity.NoticePriority priority,

        @Schema(description = "첨부 파일 URL 목록")
        List<String> attachmentUrls,

        @Schema(description = "관련 링크")
        String relatedLink
) {
    public NoticeEntity toEntity(String authorId) {
        return NoticeEntity.builder()
                .title(title)
                .content(content)
                .authorId(authorId)
                .status(NoticeEntity.NoticeStatus.ACTIVE)
                .type(type)
                .validFrom(validFrom)
                .validUntil(validUntil)
                .priority(priority)
                .attachmentUrls(attachmentUrls)
                .build();
    }
}