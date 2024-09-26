package com.pawalert.backend.domain.comment.repository;

import com.pawalert.backend.domain.comment.entity.CommentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CommentRepository extends MongoRepository<CommentEntity, String> {
    // 게시글 id 로 찾기,
    // 삭제되지 않은 댓글만 조회
    Page<CommentEntity> findByMissingReportId(Long missingReportId, Pageable pageable);


}




