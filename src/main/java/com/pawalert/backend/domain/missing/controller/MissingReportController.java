package com.pawalert.backend.domain.missing.controller;

import com.pawalert.backend.domain.missing.model.*;
import com.pawalert.backend.domain.missing.service.MissingReportService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/missing")
@RequiredArgsConstructor
public class MissingReportController {
    private final MissingReportService missingReportService;

    //실종 글 작성
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<List<String>>> createMissingReport(@AuthenticationPrincipal CustomUserDetails user,
                                                                             @RequestPart("MissingPost") MissingReportRecord request,
                                                                             @RequestPart("MissingImage") List<MultipartFile> images) {

        return missingReportService.createMissingReport(request, user, images);
    }

    // 실종글 수정
    @PatchMapping("/update")
    public ResponseEntity<?> updateMissingReport(@AuthenticationPrincipal CustomUserDetails user,
                                                 @RequestPart("MissingUpdatePost") MissingUpdateRequest request,
                                                 @RequestPart("MissingUpdateImage") List<MultipartFile> images) {

        return missingReportService.updateMissingReport(request, user, images);

    }

    // 실종글 삭제
    @DeleteMapping("/delete/{missingReportId}")
    public ResponseEntity<String> deleteMissingReport(@AuthenticationPrincipal CustomUserDetails user,
                                                      @PathVariable Long missingReportId) {

        return missingReportService.deleteMissingReport(missingReportId, user);

    }

    // 실종 글 상세 조회
    @GetMapping("/getdetail/{missingReportId}")
    public ResponseEntity<SuccessResponse<MissingDetailResponse>> getMissingReportDetail(@AuthenticationPrincipal CustomUserDetails user,
                                                                                         @PathVariable Long missingReportId) {

        return missingReportService.getMissingReportDetail(missingReportId, user);
    }

    //실종상태변경
    @PatchMapping("/change")
    public ResponseEntity<SuccessResponse<String>> changeMissingStatus(@AuthenticationPrincipal CustomUserDetails user,
                                                                       @RequestBody ChangeMissingStatusRecord request) {

        return missingReportService.changeMissingStatus(request, user);

    }


    //todo : 실종신고 검색 조회
    //todo : 실종신고 검색 조회
    @GetMapping("/search")
    public Page<MissingViewListResponse> searchMissingReports(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String addressDetail1,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 10) Pageable pageable) {

        MissingViewListRequest request = new MissingViewListRequest(address, addressDetail1, status);
        return missingReportService.getMissingReports(request, pageable);
    }


}
