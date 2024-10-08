package com.pawalert.backend.domain.user.controller;

import com.pawalert.backend.domain.missing.model.MissingViewListResponse;
import com.pawalert.backend.domain.shelter.entity.AnimalRescueOrganizationEntity;
import com.pawalert.backend.domain.shelter.model.ShelterUpdateOrCreateRequest;
import com.pawalert.backend.domain.user.model.UserUpdateRequest;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.domain.user.service.LoginMemberUserService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/user")
public class LoginMemberUserController {

    private final LoginMemberUserService userService;
    private final UserRepository userRepository;


    // 내 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.getMyPage(customUserDetails);
    }

    // 내 정보 업데이트
    @PatchMapping("/update")
    public ResponseEntity<?> updateMyPage(@AuthenticationPrincipal CustomUserDetails user,
                                          @RequestBody UserUpdateRequest request
    ) {
        return userService.updateMyPage(request, user);
    }

    // 프로필 이미지 업데이트
    @PatchMapping("/updateProfileImage")
    public ResponseEntity<SuccessResponse<String>> updateProfileImage(@AuthenticationPrincipal CustomUserDetails user,
                                                                      @RequestPart("userImage") MultipartFile images) {
        return userService.updateProfileImage(user, images);
    }


    // 보호센터 가입 (회원)
    @PostMapping(value = "/shelterSignup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<String>>  createShelterMember(@AuthenticationPrincipal CustomUserDetails user,
                                                              @RequestPart("shelter") ShelterUpdateOrCreateRequest request,
                                                              @RequestPart("image") MultipartFile file
    ) {
        return userService.createShelter(user, request, file);
    }

    //내가 작성한 글조회
    @GetMapping("/myposts")
    public ResponseEntity<SuccessResponse<List<MissingViewListResponse>>> myPosts(@AuthenticationPrincipal CustomUserDetails user) {
        return userService.getMyPosts(user);
    }

}
