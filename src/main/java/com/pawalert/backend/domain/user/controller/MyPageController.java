package com.pawalert.backend.domain.user.controller;

import com.pawalert.backend.domain.missing.model.MissingViewListResponse;
import com.pawalert.backend.domain.user.service.MyPageService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    // 내가 작성한 실종 게시글
    @GetMapping("/myposts")
    public ResponseEntity<SuccessResponse<List<MissingViewListResponse>>> myPosts(@AuthenticationPrincipal CustomUserDetails user) {
        return myPageService.getMyPosts(user);
    }


}
