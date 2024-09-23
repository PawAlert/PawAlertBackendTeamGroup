package com.pawalert.backend.domain.user.service;

import com.pawalert.backend.domain.missing.model.MissingViewListResponse;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.domain.missing.repository.MissingReportRepository;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MissingReportRepository missingReportRepository;


    // 내가 작성한 글
    public ResponseEntity<SuccessResponse<List<MissingViewListResponse>>> getMyPosts(CustomUserDetails user) {
        List<MissingViewListResponse> response = missingReportRepository.findEqualMissingReportsId(user.getId());
        return ResponseHandler.generateResponse(HttpStatus.OK, "내가 작성한 글 조회 성공", response);
    }
}
