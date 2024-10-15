package com.pawalert.backend.domain.missing.controller;

import com.pawalert.backend.domain.missing.model.*;
import com.pawalert.backend.domain.missing.service.MissingReportService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "실종 게시글 작성", description = "실종된 글 관련 API")
@RestController
@RequestMapping("/api/missing")
@RequiredArgsConstructor
public class MissingReportController {
    private final MissingReportService missingReportService;

    //실종 글 작성
    @PostMapping(value = "/create")
    @Operation(summary = "실종 게시글 작성", description = "반려동물 찾기 페이지에서 실종글을 등록합니다")
    public ResponseEntity<SuccessResponse<List<String>>> createMissingReport(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody MissingReportRecord request
    ) {
        return missingReportService.createMissingReport(request, user);
    }


    // 실종글 수정
    @PatchMapping("/update")
    @Operation(summary = "실종 글을 수정합니다.", description = "실종 글 수정하기")
    public ResponseEntity<?> updateMissingReport(@AuthenticationPrincipal CustomUserDetails user,
                                                 @RequestBody MissingPatchRequest request
    ) {
        return missingReportService.updateMissingReport(request, user);
    }

    // 실종글 삭제
    @DeleteMapping("/delete/{missingReportId}")
    @Operation(summary = "실종 글을 삭제합니다.", description = "실종 글 삭제하기")
    public ResponseEntity<String> deleteMissingReport(@AuthenticationPrincipal CustomUserDetails user,
                                                      @PathVariable Long missingReportId) {

        return missingReportService.deleteMissingReport(missingReportId, user);
    }

    // 실종 글 상세 조회
    @GetMapping("/getdetail/{missingReportId}")
    @Operation(summary = "실종 상세 조회", description = "실종 글을 단 건으로 상세조회 합니다.")
    public ResponseEntity<SuccessResponse<MissingDetailResponse>> getMissingReportDetail(@AuthenticationPrincipal CustomUserDetails user,
                                                                                         @PathVariable Long missingReportId) {

        return missingReportService.getMissingReportDetail(missingReportId, user);
    }

    //실종상태변경
    @PatchMapping("/change")
    @Operation(summary = "실종 글의 상태변경.", description = "현재 실종 상태를 변경합니다.")
    public ResponseEntity<SuccessResponse<String>> changeMissingStatus(@AuthenticationPrincipal CustomUserDetails user,
                                                                       @RequestBody ChangeMissingStatusRecord request) {

        return missingReportService.changeMissingStatus(request, user);

    }


    // 실종신고 전체 조회
    @GetMapping("/list")
    @Operation(summary = "실종 글 목록을 조회합니다.", description = "삭제되지 않은 실종 글을 전체 조회합니다. 페이지 네이션 적용")
    public Page<MissingReportNewListResponse> missingPostList(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "desc") String sortDirection, // 정렬 방향
            @RequestParam(value = "statusFilter", required = false, defaultValue = "MISSING") String statusFilter) { // 상태 필터
        return missingReportService.getMissingReports(pageable, sortDirection, statusFilter);
    }


}
