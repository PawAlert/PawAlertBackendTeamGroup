package com.pawalert.backend.domain.missing.controller;

import com.pawalert.backend.domain.missing.model.MissingReportRecord;
import com.pawalert.backend.domain.missing.service.MissingReportService;
import com.pawalert.backend.domain.mypet.model.PetUpdateRequest;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/missing")
@RequiredArgsConstructor
public class MissingReportController {
    private final MissingReportService missingReportService;

    //todo : 실종신고 등록
    @PostMapping(value = "/updateMissingReport", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createMissingReport(@AuthenticationPrincipal CustomUserDetails user,
                                    @RequestPart("MissingPost") MissingReportRecord request,
                                    @RequestPart("MissingImage") List<MultipartFile> images) {

        missingReportService.createMissingReport(request, user, images);

    }


    //todo : 실종신고 수정
    //todo : 실종신고 삭제
    //todo : 실종신고 상세 조회
    //todo : 실종신고 전체 조회
    //todo : 실종신고 검색 조회
    //todo : 실종신고 상태 변경

    //todo : 실종신고 등록하기 진입 시, 정보 전달(예: 내 펫정보)

}
