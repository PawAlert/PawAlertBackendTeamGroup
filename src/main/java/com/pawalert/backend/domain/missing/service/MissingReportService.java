package com.pawalert.backend.domain.missing.service;


import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.domain.missing.entity.MissingReportImageEntity;
import com.pawalert.backend.domain.missing.model.*;
import com.pawalert.backend.domain.missing.repository.MissingImageRepository;
import com.pawalert.backend.domain.missing.repository.MissingReportRepository;
import com.pawalert.backend.domain.mypet.entity.PetEntity;
import com.pawalert.backend.domain.mypet.model.PetImageListRecord;
import com.pawalert.backend.domain.mypet.repository.PetRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.Location;
import com.pawalert.backend.global.SaveImage;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MissingReportService {
    private final UserRepository userRepository;
    private final MissingReportRepository missingReportRepository;
    private final SaveImage saveImage;
    private final PetRepository petRepository;
    private final MissingImageRepository missingImageRepository;

    // 실종글 수정
    @Transactional
    public ResponseEntity<SuccessResponse<String>> updateMissingReport(
            MissingUpdateRequest request,
            CustomUserDetails user,
            List<MultipartFile> images) {

        // 사용자 정보를 가져옵니다.
        UserEntity userMember = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        // 게시글 정보를 가져옵니다.
        MissingReportEntity missingReport = missingReportRepository.findById(request.missingReportId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MISSING_REPORT));

        // 게시글의 이미지를 가져옵니다.
        List<MissingReportImageEntity> missingImages = missingReport.getMissingPetImages();

        // 게시글 작성자가 현재 사용자와 일치하는지 확인합니다.
        if (!missingReport.getUser().getId().equals(userMember.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 게시글 정보를 업데이트합니다.
        missingReport.setTitle(request.title());
        missingReport.setContent(request.content());
        missingReport.setDescription(request.description());
        missingReport.setStatus(request.status());
        missingReport.setRewardAmount(request.rewardAmount());

        // 삭제할 이미지 처리
        if (!request.deleteImageIdList().isEmpty()) {
            for (Long imageId : request.deleteImageIdList()) {
                MissingReportImageEntity image = missingImages.stream()
                        .filter(img -> img.getId().equals(imageId))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException(ErrorCode.UPLOAD_ERROR_IMAGE));

                // 이미지 소유자가 현재 사용자와 일치하는지 확인합니다.
                if (!image.getMissingReport().getUser().equals(userMember)) {
                    throw new BusinessException(ErrorCode.FORBIDDEN);
                }

                // 이미지 삭제 마킹
                image.setDeleted(true);
            }
        }

        // 새로운 이미지 저장
        List<MissingReportImageEntity> newImages = images.stream()
                .map(image -> {
                    String imageUrl = saveImage.SaveImages(image);
                    return MissingReportImageEntity.builder()
                            .missingPhotoUrl(imageUrl)
                            .missingReport(missingReport)
                            .isDeleted(false)
                            .build();
                }).toList();
        missingImageRepository.saveAll(newImages);

        // 응답 생성
        return ResponseHandler.generateResponse(HttpStatus.OK, "Missing report updated successfully", userMember.getEmail());
    }


    // 실종글 등록
    @Transactional
    public ResponseEntity<SuccessResponse<List<String>>> createMissingReport(MissingReportRecord request,
                                                                             CustomUserDetails user,
                                                                             List<MultipartFile> images) {
        UserEntity userMember = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        try {
            Location location = Location.builder()
                    .postcode(request.locataionRecord().postcode())
                    .address(request.locataionRecord().address())
                    .addressDetail(request.locataionRecord().addressDetail())
                    .latitude(request.locataionRecord().latitude())
                    .longitude(request.locataionRecord().longitude())
                    .build();


            PetEntity pet = PetEntity.builder()
                    .microchipId(request.microchipId())
                    .petName(request.petName())
                    .species(request.species())
                    .color(request.petColor())
                    .age(request.age())
                    .gender(request.petGender())
                    .description(request.petDescription())
                    .deleted(false)
                    .user(userMember)
                    .build();

            petRepository.save(pet);


            MissingReportEntity missingReport = MissingReportEntity.builder()
                    .title(request.title())
                    .content(request.content())
                    .dateLost(request.dateLost())
                    .location(location)
                    .description(request.description())
                    .status(request.status())
                    .contact1(request.contact1())
                    .contact2(request.contact2())
                    .user(userMember)
                    .pet(pet)
                    .build();

            missingReportRepository.save(missingReport);

            List<MissingReportImageEntity> imageUrls = images.stream()
                    .map(image -> {
                        String imageUrl = saveImage.SaveImages(image);
                        return MissingReportImageEntity.builder()
                                .missingPhotoUrl(imageUrl)
                                .missingReport(missingReport)
                                .isDeleted(false)
                                .build();
                    }).toList();

            missingImageRepository.saveAll(imageUrls);

            // 넘겨줄 data 정보
            List<String> data = List.of(
                    "user Email =  " + userMember.getEmail(),
                    "pet name = " + pet.getPetName()
            );
            return ResponseHandler.generateResponse(HttpStatus.CREATED, "Missing report created successfully", data);

        } catch (Exception e) {
            log.error("Missing report create error", e);
            throw new BusinessException(ErrorCode.ERROR_MISSING_REPORT);
        }


    }

    // 실종 글 상세 조회
    public ResponseEntity<SuccessResponse<MissingDetailResponse>> getMissingReportDetail(Long missingReportId, CustomUserDetails user) {
        UserEntity userMember = null;
        boolean isMine = false;
        MissingReportEntity missingReport = missingReportRepository.findById(missingReportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MISSING_REPORT));

        if (Optional.ofNullable(user).isPresent()) {
            userMember = userRepository.findByUid(user.getUid())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

            isMine = missingReport.getUser().getId().equals(userMember.getId());
        }


        MissingDetailResponse response = new MissingDetailResponse(
                missingReport.getUser().getUserName(),
                missingReport.getUser().getPhoneNumber(),
                isMine,
                missingReport.getId(),
                missingReport.getTitle(),
                missingReport.getContent(),
                missingReport.getDateLost(),
                missingReport.getLocation(),
                missingReport.getDescription(),
                missingReport.getStatus().toString(),
                missingReport.getPet().getPetName(),
                missingReport.getPet().getSpecies(),
                missingReport.getPet().isNeutering(),
                missingReport.getPet().getColor(),
                missingReport.getPet().getAge(),
                missingReport.getPet().getGender(),
                missingReport.getPet().getMicrochipId(),
                missingReport.getPet().getDescription(),
                // image 는 id 와 url 함께
                missingReport.getMissingPetImages().stream()
                        .map(image -> new PetImageListRecord(image.getId(), image.getMissingPhotoUrl()))
                        .toList(),
                missingReport.getRewardAmount(),
                missingReport.getRewardStatus()
        );
        return ResponseHandler.generateResponse(HttpStatus.OK, "Missing report detail retrieved successfully", response);

    }

    // 실종글 리스트 조회
    public Page<MissingViewListResponse> getMissingReports(Pageable pageable) {
        Page<MissingReportEntity> reportsPage = missingReportRepository.findAll(pageable);

        Page<MissingViewListResponse> responsePage = reportsPage.map(missingReport -> {
            String firstImageUrl = missingReport.getMissingPetImages().isEmpty() ?
                    null :
                    missingReport.getMissingPetImages().get(0).getMissingPhotoUrl();

            // DTO 생성
            return new MissingViewListResponse(
                    missingReport.getId(),
                    missingReport.getUser().getId(),
                    missingReport.getTitle(),
                    missingReport.getDateLost(),
                    missingReport.getLocation().getPostcode(),
                    missingReport.getLocation().getAddress(),
                    missingReport.getLocation().getAddressDetail(),
                    missingReport.getStatus().name(),
                    missingReport.getPet().getPetName(),
                    missingReport.getPet().getSpecies(),
                    missingReport.getPet().getColor(),
                    missingReport.getPet().getAge(),
                    missingReport.getPet().getGender(),
                    firstImageUrl,
                    missingReport.getContent(),
                    missingReport.getContact1()
            );
        });
        return responsePage;
    }


    // 실종글 삭제
    @Transactional
    public ResponseEntity<String> deleteMissingReport(Long missingReportId, CustomUserDetails user) {
        // 실종글 조회
        MissingReportEntity missingReport = missingReportRepository.findById(missingReportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MISSING_REPORT));

        // 실종글 작성자 확인
        if (!missingReport.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        try {
            // 삭제 마킹
            missingReport.setDeleted(true);

            // 관련 이미지 처리 (삭제 마킹)
            List<MissingReportImageEntity> images = missingReport.getMissingPetImages();
            for (MissingReportImageEntity image : images) {
                image.setDeleted(true);
            }
            missingImageRepository.saveAll(images);

            // 응답 생성
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_DELETE);
        }

    }


    // 실종 상태 변경
    @Transactional
    public ResponseEntity<SuccessResponse<String>> changeMissingStatus(ChangeMissingStatusRecord request, CustomUserDetails user) {


        MissingReportEntity missingReport = missingReportRepository.findById(request.missingReportId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MISSING_REPORT));

        if (!missingReport.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        missingReport.setStatus(request.status());

        return ResponseHandler.generateResponse(HttpStatus.OK, "Missing report status changed successfully", "변경 : " + request.status().toString());

    }

    // 상태, 주소로 조회
    public Page<MissingViewListResponse> getMissingReports(MissingViewListRequest request, Pageable pageable) {
        return missingReportRepository.searchMissingReports(request, pageable);
    }

}
