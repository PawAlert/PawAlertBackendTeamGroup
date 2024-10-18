package com.pawalert.backend.domain.volunteer.controller;

import com.pawalert.backend.domain.volunteer.dto.VolunteerActivityDetailDto;
import com.pawalert.backend.domain.volunteer.dto.VolunteerActivityRequest;
import com.pawalert.backend.domain.volunteer.dto.VolunteerActivitySearchCondition;
import com.pawalert.backend.domain.volunteer.entity.ActivityType;
import com.pawalert.backend.domain.volunteer.service.VolunteerActivityService;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/volunteer")
@RequiredArgsConstructor
@Tag(name = "봉사활동", description = "봉사활동 관련 API")
public class VolunteerActivityController {

    private final VolunteerActivityService volunteerActivityService;

    @PostMapping("/create")
    @Operation(summary = "봉사활동 글 작성", description = "새로운 봉사활동 글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "봉사활동 글 작성 성공")
    public ResponseEntity<SuccessResponse<String>> createVolunteerActivity(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody VolunteerActivityRequest request
    ) {
        try {
            String activityTitle = volunteerActivityService.createVolunteerActivity(userDetails.getUid(), request);
            return ResponseHandler.created("봉사활동 글이 성공적으로 등록되었습니다.", activityTitle);
        } catch (Exception e) {
            return ResponseHandler.internalServerError("봉사활동 글 등록 중 오류가 발생했습니다.", e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "봉사활동 상세 조회", description = "특정 ID의 봉사활동 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "봉사활동 상세 조회 성공")
    public ResponseEntity<SuccessResponse<VolunteerActivityDetailDto>> getVolunteerActivityDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        VolunteerActivityDetailDto activityDetail = volunteerActivityService.getVolunteerActivityDetail(id);
        return ResponseHandler.ok("봉사활동 상세 정보를 성공적으로 조회했습니다.", activityDetail);
    }

    @GetMapping("/search")
    @Operation(summary = "봉사활동 검색", description = "조건에 맞는 봉사활동을 검색합니다.")
    @ApiResponse(responseCode = "200", description = "봉사활동 검색 성공")
    public ResponseEntity<SuccessResponse<Page<VolunteerActivityDetailDto>>> searchVolunteerActivities(
            @RequestParam(required = false) ActivityType activityType,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "true") boolean sortByClosest,
            @PageableDefault(size = 9) Pageable pageable) {

        VolunteerActivitySearchCondition condition = new VolunteerActivitySearchCondition(
                activityType,
                province,
                city,
                sortByClosest
        );

        Page<VolunteerActivityDetailDto> result = volunteerActivityService.searchVolunteerActivities(condition, pageable);
        return ResponseHandler.ok("봉사활동 검색 결과를 성공적으로 조회했습니다.", result);
    }
}