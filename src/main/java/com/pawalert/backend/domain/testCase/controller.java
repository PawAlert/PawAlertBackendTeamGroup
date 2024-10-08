package com.pawalert.backend.domain.testCase;

import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.domain.user.service.LoginMemberUserService;
import com.pawalert.backend.global.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor

@RequestMapping("/test")
@RestController
public class controller {

    private final S3Service s3Service;
    private final LoginMemberUserService userService;
    private final UserRepository userRepository;

    @PostMapping("/fileupload")
    public String upload(@RequestParam("image") MultipartFile file) {

        return s3Service.uploadFile(file, true);

    }



}
