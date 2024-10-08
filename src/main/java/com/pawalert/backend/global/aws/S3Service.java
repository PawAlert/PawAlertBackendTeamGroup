package com.pawalert.backend.global.aws;

import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.s3.public-folder}")
    private String publicFolderPath;

    @Value("${spring.cloud.aws.s3.private-folder}")
    private String privateFolderPath;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.aws.basic.image}")
    private String basicImage;

    public String basicProfile() {
        return basicImage;
    }

    public String uploadFile(MultipartFile file, boolean isPublic) {
        String folderPath = isPublic ? publicFolderPath : privateFolderPath;
        String fileExtension = "";
        String originalFileName = file.getOriginalFilename();

        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        String fileName = folderPath + "/" + uniqueFileName;

        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
        } catch (S3Exception | IOException e) {
            throw new BusinessException(ErrorCode.UPLOAD_ERROR_IMAGE);
        }
    }

}
