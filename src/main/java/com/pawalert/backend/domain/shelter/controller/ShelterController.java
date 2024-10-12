package com.pawalert.backend.domain.shelter.controller;

import com.pawalert.backend.domain.shelter.model.CertificationShelterResponse;
import com.pawalert.backend.domain.shelter.model.ShelterJoinDto;
import com.pawalert.backend.domain.shelter.model.ShelterViewResponse;
import com.pawalert.backend.domain.shelter.service.ShelterService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/shelter")
@RequiredArgsConstructor
@Tag(name = "보호센터 정보 수정, 조회, 인증 API 입니다", description = "auth 관련 제외한 보호센터 API 입니다.")

public class ShelterController {
    private final ShelterService shelterService;


    //     보호센터 정보 수정
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "보호센터 정보 수정 API 입니다.", description = "보호센터 정보 수정")

    public ResponseEntity<SuccessResponse<String>> updateHospitalDoctor(@AuthenticationPrincipal CustomUserDetails user,
                                                                        @RequestPart("shelterUpdate") ShelterJoinDto request,
                                                                        @RequestPart("image") MultipartFile file
    ) {
        return shelterService.updateShelter(user, request, file);
    }

    // 보호센터 정보 조회
    @GetMapping("/view/{shelterId}")
    @Operation(summary = "보호센터 정보를 조회 API 입니다.", description = "보호센터 정보 조회")

    public ResponseEntity<SuccessResponse<ShelterViewResponse>> getHospitalDoctorView(@AuthenticationPrincipal CustomUserDetails user,
                                                                                      @PathVariable Long shelterId) {
        return shelterService.getShelterView(user, shelterId);
    }

    // 보호센터 인증
    @GetMapping("/certification")
    @Operation(summary = "보호센터 인증 API 입니다.", description = "보호센터 인증")
    public ResponseEntity<SuccessResponse<String>> certificationHospitalDoctor(@RequestBody CertificationShelterResponse request) {
        return shelterService.certificationShelter(request);
    }

    // todo : 보호센터 정보 삭제는 탈퇴하면 자동 삭제

}
