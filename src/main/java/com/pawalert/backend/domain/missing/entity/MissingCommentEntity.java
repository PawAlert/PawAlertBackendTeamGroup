package com.pawalert.backend.domain.missing.entity;

import com.pawalert.backend.global.BaseEntity;
import com.pawalert.backend.domain.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class MissingCommentEntity extends BaseEntity {
    @Id
    @Column(name = "comment_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "missing_report_id", nullable = false)
    @Schema(description = "실종글에 달린 게시글 id")
    private MissingReportEntity missingReport;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "댓글을 작성한 사용자의 user_id")
    private UserEntity user;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    @Schema(description = "내용")
    private String content;



}