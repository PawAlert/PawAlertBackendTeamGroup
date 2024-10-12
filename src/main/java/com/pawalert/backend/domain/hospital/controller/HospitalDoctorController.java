package com.pawalert.backend.domain.hospital.controller;


import com.pawalert.backend.domain.hospital.dto.*;
import com.pawalert.backend.domain.hospital.service.HospitalDoctorService;
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
@RequestMapping("/api/hospital/doctor")
@RequiredArgsConstructor
@Tag(name = "동물병원 등록, 수정 조회 등등", description = "동물병원 관련 API")

public class HospitalDoctorController {
    private final HospitalDoctorService hospitalDoctorService;

    // 병원 정보 등록
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "(회원) 동물병원 회원가입", description = "회원 동물병원 회원가입정보 등록 API")

    public ResponseEntity<SuccessResponse<String>> createHospitalDoctor(@AuthenticationPrincipal CustomUserDetails user,
                                                                        @RequestPart("hospital") HospitalDoctorRequest request,
                                                                        @RequestPart("image") MultipartFile file
    ) {
        return hospitalDoctorService.createHospitalDoctor(user, request, file);
    }

    // 비회원 동물병원 정보 등록
    @PostMapping(value = "/signupCreate")
    @Operation(summary = "(비회원) 동물병원 회원가입", description = "비회원 동물병원 회원가입 정보 등록 API")

    public ResponseEntity<SuccessResponse<String>> createHospitalDoctor(@RequestBody SignupHospitalDoctorRequest request
    ) {
        return hospitalDoctorService.signupHospitalDoctor(request);
    }


    // 병원 정보 수정
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "동물병원 정보 수정", description = "동물병원 정보 수정 API")
    public ResponseEntity<SuccessResponse<String>> updateHospitalDoctor(@AuthenticationPrincipal CustomUserDetails user,
                                                                        @RequestPart("hospitalUpdate") HospitalDoctorUpdateRequest request,
                                                                        @RequestPart("image") MultipartFile file
    ) {
        return hospitalDoctorService.updateHospitalDoctor(user, request, file);
    }

    // 병원 정보 조회
    @GetMapping("/view")
    @Operation(summary = "동물병원 조회", description = "동물병원 조회 API")

    public ResponseEntity<SuccessResponse<HospitalDoctorViewResponse>> getHospitalDoctorView(@AuthenticationPrincipal CustomUserDetails user) {
        return hospitalDoctorService.getHospitalDoctorView(user);
    }

    // todo 병원 정보 삭제


    // 동물병원 인증
    @PostMapping("/certification")
    @Operation(summary = "동물병원 인증", description = "동물병원 인증 API")
    public ResponseEntity<SuccessResponse<String>> certificationHospitalDoctor(@RequestBody CertificationHospitalDoctorResponse request) {
        return hospitalDoctorService.certificationHospitalDoctor(request);
    }

}
