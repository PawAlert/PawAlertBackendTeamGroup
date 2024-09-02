package com.pawalert.backend.domain.comment.entity;

import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.domain.user.entity.UserEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comment") // MongoDB에서 "comment" 컬렉션에 저장
public class CommentEntity {

    @Id
    private String id;
    private String userId;  // MongoDB에서는 직접 사용자 ID를 저장
    private String missingReportId;  // MongoDB에서는 직접 게시글 ID를 저장
    private String content;
    private boolean isDeleted;
    private LocalDateTime timestamp;
}
