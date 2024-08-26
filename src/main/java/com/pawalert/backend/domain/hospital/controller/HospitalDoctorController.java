package com.pawalert.backend.domain.hospital.controller;


import com.pawalert.backend.domain.hospital.dto.HospitalDoctorRequest;
import com.pawalert.backend.domain.hospital.dto.HospitalDoctorUpdateRequest;
import com.pawalert.backend.domain.hospital.dto.HospitalDoctorViewResponse;
import com.pawalert.backend.domain.hospital.service.HospitalDoctorService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/hospital/doctor")
@RequiredArgsConstructor
public class HospitalDoctorController {
    private final HospitalDoctorService hospitalDoctorService;

    // 병원 정보 등록
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<String>> createHospitalDoctor(@AuthenticationPrincipal CustomUserDetails user,
                                                                        @RequestPart("hospital") HospitalDoctorRequest request,
                                                                        @RequestPart("image") MultipartFile file
    ) {
        return hospitalDoctorService.createHospitalDoctor(user, request, file);
    }
    // 병원 정보 수정
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<String>> updateHospitalDoctor(@AuthenticationPrincipal CustomUserDetails user,
                                                                        @RequestPart("hospitalUpdate") HospitalDoctorUpdateRequest request,
                                                                        @RequestPart("image") MultipartFile file
    ) {
        return hospitalDoctorService.updateHospitalDoctor(user, request, file);
    }

    // 병원 정보 조회
    @GetMapping("/view")
    public ResponseEntity<SuccessResponse<HospitalDoctorViewResponse>> getHospitalDoctorView(@AuthenticationPrincipal CustomUserDetails user) {
        return hospitalDoctorService.getHospitalDoctorView(user);
    }

    // todo 병원 정보 삭제



}
