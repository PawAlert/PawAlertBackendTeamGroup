package com.pawalert.backend.domain.mypet.controller;

import com.pawalert.backend.domain.mypet.model.PetGetResponse;
import com.pawalert.backend.domain.mypet.model.PetRegisterRequest;
import com.pawalert.backend.domain.mypet.model.PetUpdateRequest;
import com.pawalert.backend.domain.mypet.service.PetService;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;


    @PostMapping(value = "/createMyPet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createMyPet(@AuthenticationPrincipal CustomUserDetails user,
                            @RequestPart("petDto") PetRegisterRequest request,
                            @RequestPart("petImage") List<MultipartFile> images) {
        petService.createMyPet(request, user, images);
    }


    @PatchMapping(value = "/updateMyPet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateMyPet(@AuthenticationPrincipal CustomUserDetails user,
                            @RequestPart("petUpdateDto") PetUpdateRequest request,
                            @RequestPart("petImage") List<MultipartFile> images) {
        petService.updateMyPet(request, user, images);

    }

    @DeleteMapping("/deleteMyPet")
    public void deleteMyPet(Long petId, @AuthenticationPrincipal CustomUserDetails user) {
        petService.deleteMyPet(petId, user);
    }

    @GetMapping("/getMyPet")
    public PetGetResponse getMyPet(@RequestParam Long petId, @AuthenticationPrincipal CustomUserDetails user) {
        return petService.getMyPet(petId, user);
    }


}
