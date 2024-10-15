package com.pawalert.backend.domain.official.controller;

import com.pawalert.backend.domain.official.model.OfficialRegistrationDto;
import com.pawalert.backend.domain.official.model.OfficialResponseDto;
import com.pawalert.backend.domain.official.service.OfficialService;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/official")
@Tag(name = "Official", description = "기관 관련 API")
public class OfficialController {

    private final OfficialService officialService;

    @PostMapping("/register")
    @Operation(summary = "기관 등록", description = "새로운 기관을 등록합니다.")
    public ResponseEntity<SuccessResponse<String>> registerOfficial(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody OfficialRegistrationDto registrationDto) {
        String email = officialService.registerOfficial(user.getUid(), registrationDto);
        return ResponseHandler.created("기관 회원 등록이 완료되었습니다.", email);
    }

    @PutMapping("/update")
    @Operation(summary = "기관 정보 수정", description = "기존 기관 정보를 수정합니다.")
    public ResponseEntity<SuccessResponse<String>> updateOfficial(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody OfficialRegistrationDto registrationDto) {
        String email = officialService.updateOfficial(user.getUid(), registrationDto);
        return ResponseHandler.ok("기관 정보가 성공적으로 업데이트되었습니다.", email);
    }

    @GetMapping("/getOfficial")
    @Operation(summary = "기관 정보 조회", description = "현재 로그인한 사용자의 기관 정보를 조회합니다.")
    public ResponseEntity<SuccessResponse<OfficialResponseDto>> getOfficial(
            @AuthenticationPrincipal CustomUserDetails user) {
        OfficialResponseDto officialInfo = officialService.getOfficial(user.getUid());
        return ResponseHandler.ok("기관 정보를 성공적으로 조회했습니다.", officialInfo);
    }
}