package com.pawalert.backend.domain.shelter.service;

import com.pawalert.backend.domain.shelter.entity.AnimalRescueOrganizationEntity;
import com.pawalert.backend.domain.shelter.entity.AnimalShelterEntity;
import com.pawalert.backend.domain.shelter.model.CertificationShelterResponse;
import com.pawalert.backend.domain.shelter.model.ShelterJoinDto;
import com.pawalert.backend.domain.shelter.model.ShelterViewResponse;
import com.pawalert.backend.domain.shelter.repository.AnimalShelterRepository;
import com.pawalert.backend.domain.shelter.repository.ShelterRepository;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.*;
import com.pawalert.backend.global.aws.S3Service;
import com.pawalert.backend.global.aws.SaveImage;
import com.pawalert.backend.global.config.redis.RedisService;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
// todo : 진짜 중복 코드 너무 많다 날잡고 정리하자 제발 미루지말라고.. 내일 꼭하자..^^;
public class ShelterService {
    private final ShelterRepository shelterRepository;
    private final SaveImage saveImage;
    private final UserRepository userRepository;
    private final AnimalShelterRepository animalShelterRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final S3Service s3Service;

    //    // 업데이트
    @Transactional
    public ResponseEntity<SuccessResponse<String>> updateShelter(CustomUserDetails user,
                                                                 ShelterJoinDto request,
                                                                 MultipartFile file) {

        AnimalRescueOrganizationEntity shelter = shelterRepository.findById(1L)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SHELTER));

        try {

            // todo : 변수화 하자 (중복)
            String imageUpload = s3Service.basicProfile();

            Location location = Location.from(request.location());

            shelter.setJurisdiction(request.jurisdiction());
            shelter.setShelterName(request.shelterName());
            shelter.setContactPhone(request.contactPhone());
            shelter.setContactEmail(request.contactEmail());
            shelter.setWebsiteUrl(request.websiteUrl());
            shelter.setDetailAddress(location);
            shelter.setShelterProfileImage(imageUpload);

            return ResponseHandler.generateResponse(HttpStatus.OK, "보호센터 정보 수정 성공",
                    String.format("동물보호단체 email %s 유저 ID : %s",
                            shelter.getContactEmail(), shelter.getUserId()));
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_MISSING_REPORT);
        }


    }

    // 보호 센터 정보 조회
    public ResponseEntity<SuccessResponse<ShelterViewResponse>> getShelterView(CustomUserDetails user, Long shelterId) {
        AnimalRescueOrganizationEntity shelter = shelterRepository.findById(shelterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SHELTER));


        try {
            LocataionRecord location = new LocataionRecord(
                    shelter.getDetailAddress().getLatitude(),
                    shelter.getDetailAddress().getLongitude(),
                    shelter.getDetailAddress().getAddress(),
                    shelter.getDetailAddress().getAddressDetail(),
                    shelter.getDetailAddress().getPostcode()
            );




            ShelterViewResponse response = new ShelterViewResponse(
                    shelter.getId(),
                    shelter.getJurisdiction(),
                    shelter.getShelterName(),
                    shelter.getContactPhone(),
                    location,
                    shelter.getShelterProfileImage(),
                    shelter.getContactEmail(),
                    shelter.getUserId()
            );

            return ResponseHandler.generateResponse(HttpStatus.OK, "보호센터 정보 조회 성공", response);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_MISSING_REPORT);
        }
    }

    // 쉘터 인증
    public ResponseEntity<SuccessResponse<String>> certificationShelter(CertificationShelterResponse request) {
        try {
            AnimalShelterEntity result = animalShelterRepository.findByJurisdictionAndShelterName(request.jurisdiction(), request.shelterName());
            //todo : 옵셔널로 바꾸기
            if (result == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_SHELTER);
            }
//            redisService.hospitalAndShelterAttempt("Shelter", "Success", LocalDateTime.now(), "192.168.0.0.1", request.shelterName(), request.jurisdiction());
            return ResponseHandler.generateResponse(HttpStatus.OK, "보호센터 인증 성공", result.getShelterName());
        } catch (Exception e) {
//            redisService.hospitalAndShelterAttempt("Shelter", "Fail", LocalDateTime.now(), "192.168.0.0.1", request.shelterName(), request.jurisdiction());
            throw new BusinessException(ErrorCode.DUPLICATE_SHELTER);
        }

    }



}
