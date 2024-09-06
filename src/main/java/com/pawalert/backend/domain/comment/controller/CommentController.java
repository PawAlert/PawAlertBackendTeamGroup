package com.pawalert.backend.domain.comment.controller;


import com.pawalert.backend.domain.comment.dto.CommentRequestRecord;
import com.pawalert.backend.domain.comment.entity.CommentEntity;
import com.pawalert.backend.domain.comment.repository.MongoCommentRepository;
import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.domain.missing.repository.MissingReportRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class CommentController {

    private final MissingReportRepository missingReportRepository;
    private final UserRepository userRepository;
    private final MongoCommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal CustomUserDetails user,
                                        @PathVariable Long postId,
                                        @RequestBody CommentRequestRecord commentDto) {


        MissingReportEntity post = missingReportRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 사용자 확인
        UserEntity userMember = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        CommentEntity comment = CommentEntity.builder()
                .missingReportId(String.valueOf(post.getId()))
                .userId(String.valueOf(userMember.getUid()))
                .content(commentDto.content())
                .timestamp(LocalDateTime.now())
                .build();

        commentRepository.save(comment);


        // 알림 전송
        String notificationMessage = "게시글 '" + post.getTitle() + "'에 새로운 댓글이 작성되었습니다.";

        return ResponseEntity.ok("Comment added successfully");
    }
}