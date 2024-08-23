package com.pawalert.backend.domain.mypet.controller;

import com.pawalert.backend.domain.mypet.model.PetRegisterRequest;
import com.pawalert.backend.domain.mypet.service.PetService;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;


    @PostMapping(value = "/createMyPet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createMyPet(@AuthenticationPrincipal CustomUserDetails user, @RequestPart("petDto") PetRegisterRequest request,@RequestPart("petImage") List<MultipartFile> images) {
        petService.createMyPet(request, user, images);
    }
}
