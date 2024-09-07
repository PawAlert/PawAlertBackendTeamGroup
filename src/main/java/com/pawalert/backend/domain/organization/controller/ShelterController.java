package com.pawalert.backend.domain.organization.controller;

import com.pawalert.backend.domain.hospital.dto.CertificationHospitalDoctorResponse;
import com.pawalert.backend.domain.hospital.dto.HospitalDoctorViewResponse;
import com.pawalert.backend.domain.organization.model.CertificationShelterResponse;
import com.pawalert.backend.domain.organization.model.ShelterUpdateOrCreateRequest;
import com.pawalert.backend.domain.organization.model.ShelterViewResponse;
import com.pawalert.backend.domain.organization.service.ShelterService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/shelter")
@RequiredArgsConstructor
public class ShelterController {
private final ShelterService shelterService;

    // 병원 정보 등록
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<String>> createHospitalDoctor(@AuthenticationPrincipal CustomUserDetails user,
                                                                        @RequestPart("shelter")  ShelterUpdateOrCreateRequest request,
                                                                        @RequestPart("image") MultipartFile file
    ) {
        return shelterService.createShelter(user, request, file);
    }
    // 병원 정보 수정
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<String>> updateHospitalDoctor(@AuthenticationPrincipal CustomUserDetails user,
                                                                        @RequestPart("shelterUpdate") ShelterUpdateOrCreateRequest request,
                                                                        @RequestPart("image") MultipartFile file
    ) {
        return shelterService.updateShelter(user, request, file);
    }

    // 병원 정보 조회
    @GetMapping("/view/{shelterId}")
    public ResponseEntity<SuccessResponse<ShelterViewResponse>> getHospitalDoctorView(@AuthenticationPrincipal CustomUserDetails user,
                                                                                      @PathVariable Long shelterId) {
        return shelterService.getShelterView(user, shelterId);
    }

    // todo 병원 정보 삭제
    // todo 병원 정보 검색 조회

    // 동물병원 인증
    @PostMapping("/certification")
    public ResponseEntity<SuccessResponse<String>> certificationHospitalDoctor(@RequestBody CertificationShelterResponse request) {
        return shelterService.certificationShelter(request);
    }

}
