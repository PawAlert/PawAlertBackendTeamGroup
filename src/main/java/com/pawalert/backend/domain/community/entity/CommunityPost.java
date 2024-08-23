package com.pawalert.backend.domain.community.entity;

import com.pawalert.backend.global.BaseEntity;
import com.pawalert.backend.domain.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class CommunityPost extends BaseEntity {
    @Id
    @Column(name = "post_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "작성자")
    private UserEntity user;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    @Schema(description = "제목")
    private String title;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    @Schema(description = "내용")
    private String content;



}