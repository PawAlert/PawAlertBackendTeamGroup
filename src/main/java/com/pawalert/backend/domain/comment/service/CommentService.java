package com.pawalert.backend.domain.comment.service;

import com.pawalert.backend.domain.comment.dto.CommentRequestRecord;
import com.pawalert.backend.domain.comment.entity.CommentEntity;
import com.pawalert.backend.domain.comment.repository.CommentRepository;
import com.pawalert.backend.domain.missing.repository.MissingReportRepository;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


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


}
