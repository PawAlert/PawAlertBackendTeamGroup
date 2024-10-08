package com.pawalert.backend.global.aws;

import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@Slf4j
public class SaveImage {

    @Value("${file.base-url}")
    private String baseUrl;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 파일을 업로드하는 메서드 (경로 문제 해결)
    public String SaveImages(MultipartFile image) {
        try {
            // 현재 실행 중인 디렉터리 가져오기
            String currentDir = System.getProperty("user.dir"); // 애플리케이션이 실행되는 현재 디렉터리
            Path uploadPath = Paths.get(currentDir, uploadDir); // 동적 경로로 파일 저장 경로 설정

            // 경로가 없으면 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 고유한 파일명 생성
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // 파일 저장
            image.transferTo(filePath.toFile());

            // 저장된 파일의 URL 반환
            return baseUrl + "/files/" + fileName;

        } catch (IOException e) {
            log.error("이미지 업로드 중 오류 발생", e);
            throw new BusinessException(ErrorCode.UPLOAD_ERROR_IMAGE);
        }
    }

    // todo: s3 변경 하기
    public String saveProfileImage() {
        // 기본 프로필 이미지를 반환
        return "https://static.vecteezy.com/system/resources/previews/009/292/244/non_2x/default-avatar-icon-of-social-media-user-vector.jpg";
    }
}
