package com.pawalert.backend.domain.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;
@Document(collection = "userActivities")
@Getter
@Setter
public class UserActivityMongoDb {

    @Id
    private String id;

    private String userUid;
    // 활동에 대한 구체적인 데이터 (예: 댓글 내용, 위치 등)
    private Map<String, String> activityData;
    private String userIp;
    private LocalDateTime time;



}
