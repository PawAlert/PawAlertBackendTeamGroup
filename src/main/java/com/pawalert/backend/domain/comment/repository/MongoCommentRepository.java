package com.pawalert.backend.domain.comment.repository;

import com.pawalert.backend.domain.comment.entity.CommentEntity;
import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface MongoCommentRepository extends MongoRepository<CommentEntity, String> {
    @Async
    CompletableFuture<List<CommentEntity>> findByMissingReportId(String missingReportId);
}




