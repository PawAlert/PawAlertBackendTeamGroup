package com.pawalert.backend.global.config.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 댓글 저장하기
    public void commentSaveData(String userUid, String comment, LocalDateTime createTime) throws JsonProcessingException {
        // 댓글 내용과 작성 시간을 묶어서 저장
        Map<String, String> commentData = new HashMap<>();
        commentData.put("type", "comment");
        commentData.put("comment", comment);
        commentData.put("createTime", converterTime(createTime));
        redisSaveData(userUid, commentData);

    }


    // 로그인 기록
    public void loginSaveData(String userUid, String userIp, LocalDateTime loginTime) {
        try {
            // 로그인 기록을 저장
            Map<String, String> loginData = new HashMap<>();
            loginData.put("type", "login");
            loginData.put("loginTime", converterTime(loginTime));
            loginData.put("userIp", userIp);

            // JSON 직렬화를 사용하여 List에 저장 (UID로 하나의 Key에 모든 데이터 관리)
            redisTemplate.opsForList().rightPush(
                    MessageFormat.format("user:{0}:allData", userUid), // UID 기반의 Key
                    new ObjectMapper().writeValueAsString(loginData)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();


        }

    }

    // 실종 게시글 작성 기록
    // todo : location 을 시-구-동 기준에서 어떤 것으로 할것인지
    public void missingSaveData(String userUid, Long missingReportId, String location, LocalDateTime createTime) {

        Map<String, String> missingData = new HashMap<>();
        missingData.put("missingReportId", String.valueOf(missingReportId));
        missingData.put("createTime", converterTime(createTime));
        missingData.put("location", location);

        redisSaveData(userUid, missingData);


    }

    // 동물병원, 동물병원 인증시도, 인증 성공 여부
    public void hospitalAndShelterAttempt(String type, String status, LocalDateTime time, String userIp, String name, String licenseNumber) {
        try {
            if (status.equals("Fail")) {
                Map<String, String> attemptData = new HashMap<>();
                attemptData.put("status", status);
                attemptData.put("time", converterTime(time));
                attemptData.put("userIp", userIp);
                attemptData.put("type", type);
                attemptData.put("name", name);
                attemptData.put("licenseNumber", licenseNumber);

                // MessageFormat.format 사용 방식 수정
                redisTemplate.opsForList().rightPush(
                        MessageFormat.format("{0} 인증 실패 이름 = {1}", type, name),  // 포맷팅 문자열 수정
                        new ObjectMapper().writeValueAsString(attemptData)
                );
            } else if (status.equals("Success")) {
                Map<String, String> attemptData = new HashMap<>();
                attemptData.put("status", status);
                attemptData.put("time", converterTime(time));
                attemptData.put("userIp", userIp);
                attemptData.put("type", type);
                attemptData.put("name", name);
                attemptData.put("license", licenseNumber);

                redisTemplate.opsForList().rightPush(
                        MessageFormat.format("{0} 인증 성공 이름 = {1}", type, name),  // 포맷팅 문자열 수정
                        new ObjectMapper().writeValueAsString(attemptData)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String getData(String key) {
        // Redis에서 데이터를 조회
        return (String) redisTemplate.opsForValue().get(key);
    }


    // 시간 변환
    private String converterTime(LocalDateTime time) {
        // 시간 데이터 포맷 변경
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return time.format(formatter);
    }

    // 정보 저장하기
    private void redisSaveData(String userUid, Map<String, String> saveData) {

        try {
            redisTemplate.opsForList().rightPush(
                    MessageFormat.format("user:{0}:allData", userUid), // UID 기반의 Key
                    new ObjectMapper().writeValueAsString(saveData)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
