package com.pawalert.backend.domain.shelter.service;

import com.pawalert.backend.domain.shelter.entity.AnimalRescueOrganizationEntity;
import com.pawalert.backend.domain.shelter.entity.AnimalShelterEntity;
import com.pawalert.backend.domain.shelter.model.CertificationShelterResponse;
import com.pawalert.backend.domain.shelter.model.ShelterJoinDto;
import com.pawalert.backend.domain.shelter.model.ShelterViewResponse;
import com.pawalert.backend.domain.shelter.repository.AnimalShelterRepository;
import com.pawalert.backend.domain.shelter.repository.ShelterRepository;
import com.pawalert.backend.global.*;
import com.pawalert.backend.global.aws.S3Service;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShelterService {
    private final ShelterRepository shelterRepository;
    private final AnimalShelterRepository animalShelterRepository;
    private final S3Service s3Service;

    // 업데이트
    @Transactional
    public ResponseEntity<SuccessResponse<String>> updateShelter(CustomUserDetails user,
                                                                 ShelterJoinDto request,
                                                                 MultipartFile image) {

        AnimalRescueOrganizationEntity shelter = shelterRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SHELTER));

            String imageUpload = s3Service.uploadFile(image, true);

            Location location = Location.from(request.location());

            shelter.setJurisdiction(request.jurisdiction());
            shelter.setShelterName(request.shelterName());
            shelter.setContactPhone(request.contactPhone());
            shelter.setContactEmail(request.contactEmail());
            shelter.setWebsiteUrl(request.websiteUrl());
            shelter.setLocation(location);
            shelter.setShelterProfileImage(imageUpload);

            return ResponseHandler.generateResponse(HttpStatus.OK, "보호센터 정보 수정 성공",
                    String.format("동물보호단체 email %s 유저 ID : %s",
                            shelter.getContactEmail(), shelter.getUserId()));


    }

    // 보호 센터 정보 조회
    public ResponseEntity<SuccessResponse<ShelterViewResponse>> getShelterView(CustomUserDetails user,
                                                                               Long shelterId) {
        AnimalRescueOrganizationEntity shelter = shelterRepository.findById(shelterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_SHELTER));


        try {
            // 보호센터 위치 정보
            LocataionRecord location = LocataionRecord.getLocation(shelter.getLocation());
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

        Optional<AnimalShelterEntity> result = animalShelterRepository
                .findByJurisdictionAndShelterName(request.jurisdiction(), request.shelterName());

        if (result.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_SHELTER);
        }
        return ResponseHandler.generateResponse(HttpStatus.OK,
                "보호센터 인증 성공",
                result.get().getShelterName());


    }


}
