package com.pawalert.backend.domain.comment.service;

import com.pawalert.backend.domain.comment.dto.CommentRequestRecord;
import com.pawalert.backend.domain.comment.dto.CommentResponse;
import com.pawalert.backend.domain.comment.entity.CommentEntity;
import com.pawalert.backend.domain.comment.repository.CommentRepository;
import com.pawalert.backend.domain.missing.repository.MissingReportRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    // 댓글 작성
    public ResponseEntity<SuccessResponse<Object>> createComment(CommentRequestRecord response,
                                                                 CustomUserDetails user,
                                                                 Long postId) {
        userRepository.findByUid(user.getUid()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));
        CommentEntity commentEntity = CommentEntity.builder()
                .userId(user.getUid())
                .missingReportId(postId)
                .content(response.content())
                .isDeleted(false)
                .timestamp(LocalDateTime.now())
                .build();
        commentRepository.save(commentEntity);
        return ResponseHandler.generateResponse(HttpStatus.CREATED, "Comment added successfully", null);
    }

    //댓글 조회
    public ResponseEntity<SuccessResponse<Object>> getComments(CustomUserDetails user, Long postId) {
        UserEntity userMember;

        // 회원 여부 확인 후 존재하면 유저 정보를 가져옴
        if (Optional.ofNullable(user).isPresent()) {
            userMember = userRepository.findByUid(user.getUid())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));
        } else {
            userMember = null;
        }
        List<CommentEntity> comments = commentRepository.findByMissingReportId(postId);

        List<CommentResponse> comt = comments.stream()
                .map(comment -> {
                    boolean isCommentMine = Optional.ofNullable(userMember).isPresent()
                            && userMember.getUid().equals(comment.getUserId()); // NPE 방지: userMember 사용
                    return new CommentResponse(
                            comment.getId(),
                            comment.getUserId(),
                            comment.getMissingReportId(),
                            comment.getContent(),
                            comment.isDeleted(),
                            comment.getTimestamp(),
                            isCommentMine
                    );
                }).toList();

        return ResponseHandler.generateResponse(HttpStatus.OK, "Comments fetched successfully", comt);

    }

}
