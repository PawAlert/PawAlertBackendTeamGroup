package com.pawalert.backend.domain.user.service;

import com.pawalert.backend.domain.user.entity.UserActivityMongoDb;
import com.pawalert.backend.domain.user.repository.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;


    // 특정 사용자 활동 조회
    public List<UserActivityMongoDb> getUserActivities(String userUid) {
        return userActivityRepository.findByUserUid(userUid);
    }
}
