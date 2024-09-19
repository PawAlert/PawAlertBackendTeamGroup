package com.pawalert.backend.domain.comment.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class CommentEntity {
    @Id  private String id;
    private String userId;
    private Long missingReportId;
    private String content;
    private boolean isDeleted;
    private LocalDateTime timestamp;
}
