package com.pawalert.backend.domain.user.service;

import com.pawalert.backend.domain.shelter.entity.AnimalRescueOrganizationEntity;
import com.pawalert.backend.domain.shelter.model.ShelterJoinDto;
import com.pawalert.backend.domain.shelter.repository.ShelterRepository;
import com.pawalert.backend.domain.user.controller.LoginMemberUserController;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.model.MyPageGetRequest;
import com.pawalert.backend.domain.user.model.UserRole;
import com.pawalert.backend.domain.user.model.UserUpdateRequest;
import com.pawalert.backend.global.LocataionRecord;
import com.pawalert.backend.global.aws.S3Service;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("회원유저 테스트 ")
@ExtendWith(MockitoExtension.class)
class LoginMemberUserServiceTest {

    @Mock
    private UserValidationUtil userValidationUtil;

    @Mock
    private ShelterRepository shelterRepository;

    @Mock
    private S3Service s3Service;                   // Mocked dependency
    @Mock
    private MultipartFile file;                    // Mocked MultipartFile

    @InjectMocks
    private LoginMemberUserService loginMemberUserService;


    @Test
    @DisplayName("회원 유저 보호센터 등록하기")
    void createShelter() {
        // given : 보호센터 회원가입 요청 데이터
        String testUid = "test-uid";
        CustomUserDetails mockUser = mock(CustomUserDetails.class);
        when(mockUser.getUid()).thenReturn(testUid);


        LocataionRecord location = new LocataionRecord(
                new BigDecimal("37.5665"), new BigDecimal("126.978"),
                "100-101", "서울특별시 중구 세종대로 110",
                "국립도서관 옆"
        );

        ShelterJoinDto shelterJoinDto = new ShelterJoinDto(
                "test@email", "password1234",
                "관할구역:강남동", "마음보호센터",
                "031-1234-1234", location,
                "www.test.com", "마음보호센터@gmail.com"

        );
        UserEntity mockUserEntity = UserEntity.builder()
                .uid(testUid)
                .email("test@test.com")
                .userName("testUserName")
                .phoneNumber("010-1234-5678")
                .authProvider("localuser")
                .profilePictureUrl("www.profileImage.com")
                .role(UserRole.ROLE_USER)
                .build();

        // when : userValidationUtil
        when(userValidationUtil.validateUserUid(testUid)).thenReturn(mockUserEntity);
        // 이미 등록되어 있는지 체크 - false 가 정상값
        when(shelterRepository.existsByUserId(mockUserEntity.getId())).thenReturn(false);

        // s3 기본 이미지 저장
        when(s3Service.basicProfile()).thenReturn("https://s3.example.com");

        // 메서드 호출
        ResponseEntity<SuccessResponse<String>> response = loginMemberUserService.createShelter(mockUser, shelterJoinDto, file);

        // Then : 응답 상태 코드 및 메시지 검증
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("보호센터 등록 성공", response.getBody().message());
        assertEquals("마음보호센터", response.getBody().data());

        // 보호센터 save 가 한번만 호출 되었는지
        verify(shelterRepository, times(1)).save(any(AnimalRescueOrganizationEntity.class));
        // 유저 역할이 제대로 업데이트 되었는지
        assertEquals(UserRole.ROLE_ASSOCIATION_USER, mockUserEntity.getRole());


    }

    @Test
    @DisplayName("내 정보 수정하기")
    void updateMyPage() {
        //given : 사용자 정보 및 요청 생성
        String testUid = "test-uid";
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setUid(testUid);
        mockUserEntity.setEmail("test@example.com");
        mockUserEntity.setUserName("old Name");
        mockUserEntity.setPhoneNumber("010-1111-1111");

        UserUpdateRequest request = new UserUpdateRequest("New Name", "011-9876-5432");

        // when : userValidationUtil
        when(userValidationUtil.validateUserUid(testUid)).thenReturn(mockUserEntity);

        // 실제 메서드 호출
        ResponseEntity<?> response = loginMemberUserService.updateMyPage(request, new CustomUserDetails(mockUserEntity));

        // then : 응답 상태 및 사용자 정보 확인
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("사용자 이메일 : test@example.com"));

        // 검증 : 사용자 정보가 업데이트 되었는지 확인
        assertEquals("New Name", mockUserEntity.getUserName());
        assertEquals("011-9876-5432", mockUserEntity.getPhoneNumber());

        verify(userValidationUtil).validateUserUid(testUid);

    }


    @Test
    @DisplayName("내 정보 조회하기")
    void getMyPage() {
        // given : 가짜 사용자 정보 생성
        String testUid = "test-uid";

        CustomUserDetails mockUser = mock(CustomUserDetails.class);
        when(mockUser.getUid()).thenReturn(testUid);

        UserEntity mockUserEntity = UserEntity.builder()
                .uid(testUid)
                .email("test@test.com")
                .userName("testUserName")
                .phoneNumber("010-1234-5678")
                .authProvider("localuser")
                .profilePictureUrl("www.profileImage.com")
                .role(UserRole.ROLE_USER)
                .build();

        // when : userValidationUtil
        when(userValidationUtil.validateUserUid(testUid)).thenReturn(mockUserEntity);

        // 실제 메서드 호출
        ResponseEntity<SuccessResponse<MyPageGetRequest>> response = loginMemberUserService.getMyPage(mockUser);


        // then : 결과
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().message(), "MyPage 조회 성공");
        MyPageGetRequest myPageResponse = response.getBody().data();

        assertEquals(testUid, myPageResponse.uid());
        assertEquals("test@test.com", myPageResponse.email());
        assertEquals("testUserName", myPageResponse.userName());
        assertEquals("www.profileImage.com", myPageResponse.profileImageUrl());
        assertEquals("localuser", myPageResponse.authProvider());
        assertEquals(UserRole.ROLE_USER, myPageResponse.UserRoles());

        verify(userValidationUtil).validateUserUid(testUid);
    }

    @Test
    void getMyPosts() {

    }


}