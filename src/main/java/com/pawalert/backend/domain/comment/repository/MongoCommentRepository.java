//package com.pawalert.backend.domain.comment.repository;
//
//import com.pawalert.backend.domain.comment.entity.CommentEntity;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//@Repository
//public interface MongoCommentRepository extends MongoRepository<CommentEntity, String> {
//    @Async
//    CompletableFuture<List<CommentEntity>> findByMissingReportId(String missingReportId);
//}
//
//
//
//
