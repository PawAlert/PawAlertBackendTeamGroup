package com.pawalert.backend.domain.s3Image;

import com.pawalert.backend.global.aws.S3Service;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class S3ImageUrlResponseController {
    private final S3Service s3Service;

    //이미지 여러 장
    @PostMapping(value = "/s3ImageList", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 여러장 업로드 API 입니다.", description = "여러 장 업로드 API")
    public List<String> uploadMissingImage(@AuthenticationPrincipal CustomUserDetails user,
                                           @RequestPart("image") List<MultipartFile> images
    ) {
        return images.stream()
                .map(image -> s3Service.uploadFile(image, true))
                .toList();

    }

    //이미지 한 장
    @PostMapping(value = "/s3Image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 한 장 업로드 API 입니다", description = "이미지 한장업로드 API.")
    public String uploadMissingImage(@AuthenticationPrincipal CustomUserDetails user,
                                     @RequestPart("image") MultipartFile images
    ) {
        return s3Service.uploadFile(images, true);

    }

}
