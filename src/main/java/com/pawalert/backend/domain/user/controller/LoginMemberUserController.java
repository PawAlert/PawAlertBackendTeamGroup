package com.pawalert.backend.domain.user.controller;

import com.pawalert.backend.domain.missing.model.MissingViewListResponse;
import com.pawalert.backend.domain.shelter.model.ShelterJoinDto;
import com.pawalert.backend.domain.user.model.UserUpdateRequest;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.domain.user.service.LoginMemberUserService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "내 정보 조회, 업데이트 회원변경 등등 (회원인 유저 관련 API)", description = "유저 행동 API")
public class LoginMemberUserController {

    private final LoginMemberUserService userService;
    private final UserRepository userRepository;


    // 내 정보 조회
    @GetMapping("/profile")
    @Operation(summary = "나의 정보 조회", description = "나의 정보를 가져옵니다.")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.getMyPage(customUserDetails);
    }

    // 내 정보 업데이트
    @PatchMapping("/update")
    @Operation(summary = "나의 정보 업데이트", description = "이름, 전화번호를 업데이트 합니다.")
    public ResponseEntity<?> updateMyPage(@AuthenticationPrincipal CustomUserDetails user,
                                          @RequestBody UserUpdateRequest request
    ) {
        return userService.updateMyPage(request, user);
    }

    // 프로필 이미지 업데이트
    @PatchMapping("/updateProfileImage")
    @Operation(summary = "나의 프로필 이미지 업데이트", description = "나의 프로필 이미지를 업데이트 합니다.")
    public ResponseEntity<SuccessResponse<String>> updateProfileImage(@AuthenticationPrincipal CustomUserDetails user,
                                                                      @RequestPart("userImage") MultipartFile images) {
        return userService.updateProfileImage(user, images);
    }


    // 보호센터 가입 (회원)
    @PostMapping(value = "/shelterSignup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "보호센터 회원가입", description = "보호센터 유저로 변경하는 API 입니다.")
    public ResponseEntity<SuccessResponse<String>>  createShelterMember(@AuthenticationPrincipal CustomUserDetails user,
                                                              @RequestPart("shelter") ShelterJoinDto request,
                                                              @RequestPart("image") MultipartFile file
    ) {
        return userService.createShelter(user, request, file);
    }

    //내가 작성한 글조회
    @GetMapping("/myposts")
    @Operation(summary = "나의 작성 글 조회", description = "나의 작성 글을 전체 조회 합니다. (페이지네이션으로 수정 예정)")
    public ResponseEntity<SuccessResponse<List<MissingViewListResponse>>> myPosts(@AuthenticationPrincipal CustomUserDetails user) {
        return userService.getMyPosts(user);
    }

}
