package com.pawalert.backend.domain.mypet.controller;

import com.pawalert.backend.domain.mypet.model.PetGetResponse;
import com.pawalert.backend.domain.mypet.model.PetRegisterRequest;
import com.pawalert.backend.domain.mypet.model.PetUpdateRequest;
import com.pawalert.backend.domain.mypet.model.PetViewListRequest;
import com.pawalert.backend.domain.mypet.service.PetService;
import com.pawalert.backend.global.jwt.CustomUserDetails;
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
public class PetController {
    private final PetService petService;

    // 펫 등록
    @PostMapping(value = "/createMyPet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createMyPet(@AuthenticationPrincipal CustomUserDetails user,
                            @RequestPart("petDto") PetRegisterRequest request,
                            @RequestPart("petImage") List<MultipartFile> images) {
        petService.createMyPet(request, user, images);
    }

    // 펫정보 수정
    @PatchMapping(value = "/updateMyPet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateMyPet(@AuthenticationPrincipal CustomUserDetails user,
                            @RequestPart("petUpdateDto") PetUpdateRequest request,
                            @RequestPart("petImage") List<MultipartFile> images) {
        petService.updateMyPet(request, user, images);

    }
    // 펫정보 삭제
    @DeleteMapping("/deleteMyPet")
    public void deleteMyPet(Long petId, @AuthenticationPrincipal CustomUserDetails user) {
        petService.deleteMyPet(petId, user);
    }

    //마이펫 정보 확인
    @GetMapping("/getMyPet")
    public ResponseEntity<?> getMyPet(@AuthenticationPrincipal CustomUserDetails user) {
        return petService.getPets(user);
    }

    // todo : 마이펫 리스트 조회하는 부분 추가


}
