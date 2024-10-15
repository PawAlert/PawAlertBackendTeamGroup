package com.pawalert.backend.domain.annoucement.controller;

import com.pawalert.backend.domain.annoucement.model.AnnouncementDetailDto;
import com.pawalert.backend.domain.annoucement.model.AnnouncementDto;
import com.pawalert.backend.domain.annoucement.model.AnnouncementSummaryDto;
import com.pawalert.backend.domain.annoucement.service.AnnouncementService;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    // 입양 글 작성하기
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<String>> createAnnouncement(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AnnouncementDto dto) {
        try {
            String announcementTitle = announcementService.createAnnouncement(userDetails.getUid(), dto);
            return ResponseHandler.created("공고가 성공적으로 등록되었습니다.", announcementTitle);
        } catch (Exception e) {
            return ResponseHandler.internalServerError("공고 등록 중 오류가 발생했습니다.", e.getMessage());
        }
    }

    // 입양 글 목록 가져오기
    @GetMapping("/get-announcements")
    @Operation(summary = "공고 목록 조회", description = "공고 목록을 페이지 단위로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "공고 목록 조회 성공")
    public ResponseEntity<SuccessResponse<Page<AnnouncementSummaryDto>>> getAnnouncementSummaries(
            @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AnnouncementSummaryDto> announcementPage = announcementService.getAnnouncementSummaries(pageable);
        return ResponseHandler.ok("공고 목록을 성공적으로 조회했습니다.", announcementPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "공고 상세 조회", description = "특정 ID의 공고 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "공고 상세 조회 성공")
    public ResponseEntity<SuccessResponse<AnnouncementDetailDto>> getAnnouncementDetail(
            @PathVariable Long id) {
        AnnouncementDetailDto announcementDetail = announcementService.getAnnouncementDetail(id);
        return ResponseHandler.ok("공고 상세 정보를 성공적으로 조회했습니다.", announcementDetail);
    }

    @GetMapping("/my-announcements")
    @Operation(summary = "내 공고 목록 조회", description = "로그인한 사용자의 공고 목록을 페이지 단위로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "내 공고 목록 조회 성공")
    public ResponseEntity<SuccessResponse<Page<AnnouncementSummaryDto>>> getMyAnnouncements(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AnnouncementSummaryDto> announcementPage = announcementService.getUserAnnouncements(userDetails.getUid(), pageable);
        return ResponseHandler.ok("내 공고 목록을 성공적으로 조회했습니다.", announcementPage);
    }

}