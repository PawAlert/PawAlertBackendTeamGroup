package com.pawalert.backend.domain.comment.controller;


import com.pawalert.backend.domain.comment.dto.CommentRequestRecord;
import com.pawalert.backend.domain.comment.entity.CommentEntity;
import com.pawalert.backend.domain.comment.repository.MongoCommentRepository;
import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.domain.missing.repository.MissingReportRepository;
import com.pawalert.backend.domain.notification.service.NotificationService;
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
    private final NotificationService notificationService;
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

        // 비밀 댓글 여부 및 비밀번호 처리
        String encryptedPassword = commentDto.isSecret() ? passwordEncoder.encode(commentDto.password()) : null;

        CommentEntity comment = CommentEntity.builder()
                .missingReportId(String.valueOf(post.getId()))
                .userId(String.valueOf(userMember.getId()))
                .content(commentDto.content())
                .timestamp(LocalDateTime.now())
                .isSecret(commentDto.isSecret())
                .password(encryptedPassword)
                .build();

        commentRepository.save(comment);


        // 알림 전송
        String notificationMessage = "게시글 '" + post.getTitle() + "'에 새로운 댓글이 작성되었습니다.";
        notificationService.sendNotification(post.getId().toString(), notificationMessage);

        return ResponseEntity.ok("Comment added successfully");
    }

//    @GetMapping("/{postId}/comments")
//    public ResponseEntity<List<CommentEntity>> getComments(@AuthenticationPrincipal CustomUserDetails user, @PathVariable String postId) {
//
//    }


    @PostMapping("/{postId}/comments/{commentId}/verify")
    public ResponseEntity<?> verifyCommentPassword(@PathVariable String postId, @PathVariable String commentId, @RequestBody String inputPassword) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (comment.isSecret() && passwordEncoder.matches(inputPassword, comment.getPassword())) {
            return ResponseEntity.ok(comment.getContent());
        } else {
            return ResponseEntity.status(403).body("비밀번호가 일치하지 않습니다.");
        }
    }


}