package com.pawalert.backend.domain.inquiry.controller;

import com.pawalert.backend.domain.inquiry.dto.InquiryDto;
import com.pawalert.backend.domain.inquiry.service.InquiryService;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
@Tag(name = "Inquiry", description = "문의하기 API")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("/register")
    @Operation(summary = "문의하기", description = "새로운 문의를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "문의 등록 성공",
            content = @Content(schema = @Schema(implementation = Long.class)))
    public ResponseEntity<SuccessResponse<Long>> createInquiry(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Parameter(description = "문의 정보", required = true) InquiryDto dto) {
        Long inquiryId = inquiryService.createInquiry(dto, user);
        return ResponseHandler.created("문의가 성공적으로 등록되었습니다.", inquiryId);
    }

    @GetMapping("/my-inquiries")
    @Operation(summary = "내 문의 조회", description = "현재 로그인한 사용자의 모든 문의를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "문의 조회 성공",
            content = @Content(schema = @Schema(implementation = List.class)))
    public ResponseEntity<SuccessResponse<List<InquiryDto>>> getUserInquiries(
            @AuthenticationPrincipal CustomUserDetails user) {
        List<InquiryDto> inquiries = inquiryService.getUserInquiries(user.getUid());
        return ResponseHandler.ok("문의 목록을 성공적으로 조회했습니다.", inquiries);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "모든 문의 조회 (관리자용)", description = "모든 문의를 페이지 단위로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "문의 조회 성공",
            content = @Content(schema = @Schema(implementation = Page.class)))
    public ResponseEntity<SuccessResponse<Page<InquiryDto>>> getAllInquiries(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails user) {
        Page<InquiryDto> inquiries = inquiryService.getAllInquiries(pageable);
        return ResponseHandler.ok("모든 문의 목록을 성공적으로 조회했습니다.", inquiries);
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "문의 상세 조회 (관리자용)", description = "특정 ID의 문의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "문의 상세 조회 성공",
            content = @Content(schema = @Schema(implementation = InquiryDto.class)))
    public ResponseEntity<SuccessResponse<InquiryDto>> getInquiryDetailForAdmin(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user) {
        InquiryDto inquiry = inquiryService.getInquiryDetail(id);
        return ResponseHandler.ok("문의 상세 정보를 성공적으로 조회했습니다.", inquiry);
    }

}