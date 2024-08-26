package com.pawalert.backend.global;

import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class SaveImage {

    @Value("${file.base-url}")
    private String baseUrl;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.profile-upload-dir}")
    private String profileUploadDir;

    //todo : 아니 왜 유저Entity 받음?ㅠㅠㅠㅠㅠㅠㅠㅠ이거 뭐야 ㅠㅠ 괜찮아 괜찮아...고치자..
    public @Size(max = 255) String saveProfileImage(UserEntity user) {
        // 기본 이미지 URL 반환
        return baseUrl + profileUploadDir;
    }

    public String SaveImages(MultipartFile images) {
        try {
            //파일 경로 가져옴
            Path uploadPath = Paths.get(System.getProperty("user.dir") + uploadDir);

            //파일 경로에 없으면 만듬
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + images.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // 파일 저장
            images.transferTo(filePath.toFile());

            // 저장된 파일의 URL 반환
            return baseUrl + fileName;


        } catch (IOException e) {
            throw new BusinessException(ErrorCode.UPLOAD_ERROR_IMAGE);
        }

    }

}
