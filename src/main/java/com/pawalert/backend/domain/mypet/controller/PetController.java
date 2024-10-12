package com.pawalert.backend.domain.mypet.controller;

import com.pawalert.backend.domain.mypet.model.PetGetResponse;
import com.pawalert.backend.domain.mypet.model.PetRegisterRequest;
import com.pawalert.backend.domain.mypet.model.PetUpdateRequest;
import com.pawalert.backend.domain.mypet.model.PetViewListRequest;
import com.pawalert.backend.domain.mypet.service.PetService;
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

import java.util.List;

@RestController
@RequestMapping("/api/pet")
@RequiredArgsConstructor
@Tag(name = "반려동물 관련 API", description = "반려 동믈 등록, 수정 등등")

public class PetController {
    private final PetService petService;

    // 펫 등록
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "반려동물 등록 API 입니다.", description = "반려동물 등록 API")
    public ResponseEntity<SuccessResponse<String>> createMyPet(@AuthenticationPrincipal CustomUserDetails user,
                                                               @RequestPart("petDto") PetRegisterRequest request,
                                                               @RequestPart("petImage") List<MultipartFile> images) {
       return petService.createMyPet(request, user, images);
    }

    // 펫정보 수정
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "반려동물 정보 수정 API 입니다.", description = "반려동물 수정 API")
    public ResponseEntity<SuccessResponse<String>> updateMyPet(@AuthenticationPrincipal CustomUserDetails user,
                            @RequestPart("petUpdateDto") PetUpdateRequest request,
                            @RequestPart("petImage") List<MultipartFile> images) {
       return petService.updateMyPet(request, user, images);
    }

    //마이펫 정보 확인
    @GetMapping("/getmypet")
    @Operation(summary = "반려동물 등록된 목록 확인", description = "반려동물 확인 API")

    public ResponseEntity<?> getMyPet(@AuthenticationPrincipal CustomUserDetails user) {
        return petService.getPets(user);
    }

    // 펫정보 삭제
    @DeleteMapping("/deletemypet/{petId}")
    @Operation(summary = "반려동물 정보 삭제", description = "반려동물 정보 삭제 API")
    public ResponseEntity<String> deleteMyPet(@PathVariable Long petId,
                                              @AuthenticationPrincipal CustomUserDetails user) {
       return petService.deleteMyPet(petId, user);
    }



    // todo : 마이펫 리스트 조회하는 부분 추가


}
