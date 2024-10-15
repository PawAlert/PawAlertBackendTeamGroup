package com.pawalert.backend.domain.Notice.controller;

import com.pawalert.backend.domain.Notice.model.NoticeDto;
import com.pawalert.backend.domain.Notice.service.NoticeService;
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
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Tag(name = "Notice", description = "공지사항 API")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/register")
    @Operation(summary = "공지사항 생성", description = "새로운 공지사항을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "공지사항 생성 성공",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<SuccessResponse<String>> createNotice(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Parameter(description = "공지사항 정보", required = true) NoticeDto dto) {
        String noticeTitle = noticeService.createNotice(userDetails.getUid(), dto);
        return ResponseHandler.created("공지사항이 성공적으로 등록되었습니다.", noticeTitle);
    }

    @GetMapping("/getNotices")
    @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 페이지 단위로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "공지사항 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = Page.class)))
    public ResponseEntity<SuccessResponse<Page<NoticeDto>>> getAllNotices(
            @PageableDefault(size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            }) Pageable pageable) {
        Page<NoticeDto> noticePage = noticeService.getAllNotices(pageable);
        return ResponseHandler.ok("공지사항 목록을 성공적으로 조회했습니다.", noticePage);
    }
}