package com.pawalert.backend.domain.user.repository;

import com.pawalert.backend.domain.user.entity.UserActivityMongoDb;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserActivityRepository extends MongoRepository<UserActivityMongoDb, String> {

    List<UserActivityMongoDb> findByUserUid(String userUid);
}
