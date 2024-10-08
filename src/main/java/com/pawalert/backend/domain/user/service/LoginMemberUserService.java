package com.pawalert.backend.domain.user.service;

import com.pawalert.backend.domain.missing.model.MissingViewListResponse;
import com.pawalert.backend.domain.missing.repository.MissingReportRepository;
import com.pawalert.backend.domain.shelter.entity.AnimalRescueOrganizationEntity;
import com.pawalert.backend.domain.shelter.model.ShelterJoinDto;
import com.pawalert.backend.domain.shelter.repository.ShelterRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.model.*;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.aws.S3Service;
import com.pawalert.backend.global.aws.SaveImage;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import com.pawalert.backend.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LoginMemberUserService {


    private final UserRepository userRepository;
    private final MissingReportRepository missingReportRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    // 유저 검증 유틸
    private final UserValidationUtil userValidationUtil;
    private final S3Service s3Service;

    // ---
    private final ShelterRepository shelterRepository;
    private final SaveImage saveImage;


    // 마이페이지 수정
    @Transactional
    public ResponseEntity<?> updateMyPage(UserUpdateRequest request, CustomUserDetails user) {
        // 사용자 정보 조회
        UserEntity userInfo = userValidationUtil.validateUserUid(user.getUid());

        // 사용자 정보 업데이트
        userInfo.setUserName(request.username());
        userInfo.setPhoneNumber(request.phoneNumber());

        // 성공 응답 반환
        return ResponseHandler.generateResponse(HttpStatus.OK, "내 정보 수정 성공", "사용자 이메일 : "
                + userInfo.getEmail());

    }


    // myPage 조회
    public ResponseEntity<SuccessResponse<MyPageGetRequest>> getMyPage(CustomUserDetails user) {
        // 사용자 정보 조회
        UserEntity userInfo = userValidationUtil.validateUserUid(user.getUid());

        // MyPage 응답 생성
        MyPageGetRequest response = new MyPageGetRequest(
                userInfo.getUid(),
                userInfo.getEmail(),
                userInfo.getUserName(),
                userInfo.getPhoneNumber(),
                userInfo.getAuthProvider(),
                userInfo.getProfilePictureUrl(),
                userInfo.getRole()
        );

        // 성공 응답 반환
        return ResponseHandler.generateResponse(HttpStatus.OK, "MyPage 조회 성공", response);
    }

    // 프로필 이미지 업데이트
    @Transactional
    public ResponseEntity<SuccessResponse<String>> updateProfileImage(CustomUserDetails user, MultipartFile images) {
        // 사용자 정보 조회
        UserEntity userInfo = userValidationUtil.validateUserUid(user.getUid());

        userInfo.setProfilePictureUrl(s3Service.uploadFile(images, true));

        // 성공 응답 반환
        return ResponseHandler.generateResponse(HttpStatus.OK, "프로필 이미지 업데이트 성공", "사용자 이메일 : "
                + userInfo.getEmail());
    }

    // 나의 게시글 목록 조회
    public ResponseEntity<SuccessResponse<List<MissingViewListResponse>>> getMyPosts(CustomUserDetails user) {
        List<MissingViewListResponse> response = missingReportRepository.findEqualMissingReportsId(user.getId());
        return ResponseHandler.generateResponse(HttpStatus.OK, "내가 작성한 글 조회 성공", response);
    }


    // 이미 회원인 유저 보호센터 등록하기
    @Transactional
    public ResponseEntity<SuccessResponse<String>> createShelter(CustomUserDetails user,
                                                                 ShelterJoinDto request,
                                                                 MultipartFile file) {

        // 유저 정보 가져옴,
        UserEntity userInfo = userValidationUtil.validateUserUid(user.getUid());
        // 이미 보호센터가 등록이 되었는지 존재 유무
        Boolean existsUser = shelterRepository.existsByUserId(userInfo.getId());

        if (Boolean.TRUE.equals(existsUser)) {
            throw new BusinessException(ErrorCode.EXIST_SHELTER);
        }
        String imageUrl = saveImage.saveProfileImage();

        String imageUpload = s3Service.basicProfile();


        AnimalRescueOrganizationEntity shelter = request.toEntity(userInfo.getId(), imageUpload);
        shelterRepository.save(shelter);

        if (!file.isEmpty()) {
            imageUrl = saveImage.SaveImages(file);
        }

        userInfo.setRole(UserRole.ROLE_ASSOCIATION_USER);

        return ResponseHandler.generateResponse(HttpStatus.CREATED, "보호센터 등록 성공", shelter.getShelterName());


    }

}
