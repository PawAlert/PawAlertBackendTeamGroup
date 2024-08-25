package com.pawalert.backend.domain.missing.controller;

import com.pawalert.backend.domain.missing.model.ChangeMissingStatusRecord;
import com.pawalert.backend.domain.missing.model.MissingDetailResponse;
import com.pawalert.backend.domain.missing.model.MissingReportRecord;
import com.pawalert.backend.domain.missing.model.MissingUpdateRequest;
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

    //실종신고 등록
    @PostMapping(value = "/createMissingReport", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createMissingReport(@AuthenticationPrincipal CustomUserDetails user,
                                    @RequestPart("MissingPost") MissingReportRecord request,
                                    @RequestPart("MissingImage") List<MultipartFile> images) {

        missingReportService.createMissingReport(request, user, images);

    }

    // 실종글 수정
    @PatchMapping("/updateMissingReport")
    public void updateMissingReport(@AuthenticationPrincipal CustomUserDetails user,
                                    @RequestPart("MissingUpdatePost") MissingUpdateRequest request,
                                    @RequestPart("MissingUpdateImage") List<MultipartFile> images) {

        missingReportService.updateMissingReport(request, user, images);

    }

    // 실종글 삭제
    @DeleteMapping("/deleteMissingReport/{missingReportId}")
    public void deleteMissingReport(@AuthenticationPrincipal CustomUserDetails user,
                                    @PathVariable Long missingReportId) {

        missingReportService.deleteMissingReport(missingReportId, user);

    }

    //실종상태변경
    @PatchMapping("/changeMissingStatus")
    public void changeMissingStatus(@AuthenticationPrincipal CustomUserDetails user,
                                    ChangeMissingStatusRecord request) {

        missingReportService.changeMissingStatus(request, user);

    }

    //todo : 실종신고 상세 조회 / 내 글인지 true,false
    @GetMapping("/getMissingReportDetail/{missingReportId}")
    public MissingDetailResponse getMissingReportDetail(@AuthenticationPrincipal CustomUserDetails user,
                                                        @PathVariable Long missingReportId) {

        return missingReportService.getMissingReportDetail(missingReportId, user);
    }

    //todo : 실종신고 검색 조회



}
