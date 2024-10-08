package com.pawalert.backend.global.config.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;
    public void trackUserActivity(String userUid, String userIp, String methodName, LocalDateTime activityTime) {
        Map<String, String> activityData = new HashMap<>();
        activityData.put("uid", userUid);
        activityData.put("userIp", userIp);
        activityData.put("methodName", methodName);
        activityData.put("activityTime", converterTime(activityTime));

        saveDataToRedis(userUid, activityData);
    }

    // Redis에 데이터 저장
    private void saveDataToRedis(String userUid, Map<String, String> activityData) {
        // Redis 키 생성
        String key = "user_activity:" + userUid;

        // Redis에 추가하거나 업데이트
        redisTemplate.opsForList().rightPush(key, activityDataToJson(activityData));
    }

    // 활동 데이터를 JSON으로 변환
    private String activityDataToJson(Map<String, String> activityData) {
        // JSON 변환을 위한 라이브러리 사용 (예: Gson)
        Gson gson = new Gson();
        return gson.toJson(activityData);
    }
    // 댓글 저장하기
    // todo : 댓글은 바로 mongoDB에 저장 
//    public void commentSaveData(String userUid, String comment, LocalDateTime createTime) throws JsonProcessingException {
//        // 댓글 내용과 작성 시간을 묶어서 저장
//        Map<String, String> commentData = new HashMap<>();
//        commentData.put("type", "comment");
//        commentData.put("comment", comment);
//        commentData.put("createTime", converterTime(createTime));
//        redisSaveData(userUid, commentData);
//
//    }
//
//    // 회원가입 기록
//    public void signupSavaData(String userUid, String userIp, LocalDateTime signupTime) {
//        Map<String, String> signupData = new HashMap<>();
//        signupData.put("uid", userUid);
//        signupData.put("userIp", userIp);
//        signupData.put("joinTime", converterTime(signupTime));
//        signupData.put("type", "SIGNUP");
//
//        redisSaveData(userUid, signupData);
//    }
//
//    // 로그인 기록
//    public void loginSaveData(String userUid, String userIp, LocalDateTime loginTime) {
//        // 로그인 기록을 저장
//        Map<String, String> loginData = new HashMap<>();
//        loginData.put("uid", userUid);
//        loginData.put("type", "LOGIN");
//        loginData.put("loginTime", converterTime(loginTime));
//        loginData.put("userIp", userIp);
//
//        redisSaveData(userUid, loginData);
//
//    }
//
//    // 실종 게시글 작성 기록
//    // todo : location 을 시-구-동 기준에서 어떤 것으로 할것인지
//    public void missingSaveData(String userUid, Long missingReportId, String location, LocalDateTime createTime) {
//
//        Map<String, String> missingData = new HashMap<>();
//        missingData.put("missingReportId", String.valueOf(missingReportId));
//        missingData.put("createTime", converterTime(createTime));
//        missingData.put("location", location);
//        redisSaveData(userUid, missingData);
//    }
//
//    // 병원 보호센터 관계자 회원가입
//    public void hospitalAndShelterSignup(String type, LocalDateTime time, String uid) {
//        Map<String, String> signupData = new HashMap<>();
//        signupData.put("userUid", uid);
//        signupData.put("createTime", converterTime(time));
//        signupData.put("type", type);
//
//        redisSaveData(uid, signupData);
//    }
//
//    // 동물병원, 동물병원 인증시도, 인증 성공 여부
//    public void hospitalAndShelterAttempt(String type, String status, LocalDateTime time, String userIp, String name, String licenseNumber) {
//        try {
//            if (status.equals("Fail")) {
//                Map<String, String> attemptData = new HashMap<>();
//                attemptData.put("status", status);
//                attemptData.put("time", converterTime(time));
//                attemptData.put("userIp", userIp);
//                attemptData.put("type", type);
//                attemptData.put("name", name);
//                attemptData.put("licenseNumber", licenseNumber);
//
//                // MessageFormat.format 사용 방식 수정
//                redisTemplate.opsForList().rightPush(
//                        MessageFormat.format("{0} 인증 실패 이름 = {1}", type, name),  // 포맷팅 문자열 수정
//                        new ObjectMapper().writeValueAsString(attemptData)
//                );
//            } else if (status.equals("Success")) {
//                Map<String, String> attemptData = new HashMap<>();
//                attemptData.put("status", status);
//                attemptData.put("time", converterTime(time));
//                attemptData.put("userIp", userIp);
//                attemptData.put("type", type);
//                attemptData.put("name", name);
//                attemptData.put("license", licenseNumber);
//
//                redisTemplate.opsForList().rightPush(
//                        MessageFormat.format("{0} 인증 성공 이름 = {1}", type, name),  // 포맷팅 문자열 수정
//                        new ObjectMapper().writeValueAsString(attemptData)
//                );
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
    // 시간 변환
    private String converterTime(LocalDateTime time) {
        // 시간 데이터 포맷 변경
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return time.format(formatter);
    }
//
//    // 유저 활동 정보 저장하기
//    private void redisSaveData(String userUid, Map<String, String> saveData) {
//
//        try {
//            redisTemplate.opsForList().rightPush(
//                    MessageFormat.format("user:{0}:allData", userUid), // UID 기반의 Key
//                    new ObjectMapper().writeValueAsString(saveData)
//            );
//
//            Long dataSize = redisTemplate.opsForList().size(MessageFormat.format("user:{0}:allData", userUid));
//            // 임계값 설정 (예: 회원별 데이터 100개 이상이면 MongoDB로 옮김)
//            int threshold = 10;
//
//            if (dataSize != null && dataSize >= threshold) {
//                // Redis에 있는 데이터를 MongoDB로 옮김
//                // 키에 해당하는 값, 데이터 끝까지 가져오기
//                List<Object> redisData = redisTemplate.opsForList().range(MessageFormat.format("user:{0}:allData", userUid), 0, -1);
//                if (redisData != null && !redisData.isEmpty()) {
//                    for (Object data : redisData) {
//                        mongoTemplate.save(objectMapper.readValue((String) data, Map.class), "userActivities");
//                    }
//                }
//                // Redis에서 데이터 삭제
//                redisTemplate.delete(MessageFormat.format("user:{0}:allData", userUid));
//                log.info("MongoDB로 데이터 이동 완료 및 Redis에서 삭제 완료");
//            }
//        } catch (JsonProcessingException e) {
//            // todo : 에러 관련 처리
//            e.printStackTrace();
//        }
//
//    }
}
