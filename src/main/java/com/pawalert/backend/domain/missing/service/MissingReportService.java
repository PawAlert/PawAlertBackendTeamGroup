package com.pawalert.backend.domain.missing.service;


import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.domain.missing.entity.MissingReportImageEntity;
import com.pawalert.backend.domain.missing.model.ChangeMissingStatusRecord;
import com.pawalert.backend.domain.missing.model.MissingDetailResponse;
import com.pawalert.backend.domain.missing.model.MissingReportRecord;
import com.pawalert.backend.domain.missing.model.MissingUpdateRequest;
import com.pawalert.backend.domain.missing.repository.MissingImageRepository;
import com.pawalert.backend.domain.missing.repository.MissingReportRepository;
import com.pawalert.backend.domain.mypet.entity.PetEntity;
import com.pawalert.backend.domain.mypet.model.PetImageListRecord;
import com.pawalert.backend.domain.mypet.repository.PetRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.Location;
import com.pawalert.backend.global.SaveImage;
import com.pawalert.backend.global.exception.BusinessException;
import com.pawalert.backend.global.exception.ErrorCode;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public void updateMissingReport(MissingUpdateRequest request,
                                    CustomUserDetails user,
                                    List<MultipartFile> images) {

        UserEntity userMember = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        MissingReportEntity missingReport = missingReportRepository.findById(request.missingReportId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MISSING_REPORT));

        List<MissingReportImageEntity> missingImages = missingReport.getMissingPetImages();

        if (!missingReport.getUser().getId().equals(userMember.getId())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_BOARD);
        }

        missingReport.setTitle(request.title());
        missingReport.setContent(request.content());
        missingReport.setDescription(request.description());
        missingReport.setStatus(request.status());
        missingReport.setRewardAmount(request.rewardAmount());

        // 삭제 처리 false -> true
        if (!request.deleteImageIdList().isEmpty()) {
            for (Long imageUrls : request.deleteImageIdList()) {
                missingImages.stream()
                        .filter(image -> image.getId().equals(imageUrls))
                        .forEach(image -> {
                            image.setDeleted(true);
                        });
            }
        }

        // 이미지 저장
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

    }

    // 실종글 등록
    @Transactional
    public void createMissingReport(MissingReportRecord request,
                                    CustomUserDetails user,
                                    List<MultipartFile> images) {
        UserEntity userMember = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));


        Location location = Location.builder()
                .latitude(request.latitude())
                .longitude(request.longitude())
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
                .user(userMember)
                .pet(pet)
                .rewardAmount(request.rewardAmount())
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


    }

    // 실종 글 상세 조회
    public MissingDetailResponse getMissingReportDetail(Long missingReportId, CustomUserDetails user) {

        boolean isMine = false;

        UserEntity userMember = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        MissingReportEntity missingReport = missingReportRepository.findById(missingReportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MISSING_REPORT));

        if (missingReport.getUser().getId().equals(userMember.getId())) {
            isMine = true;
        }

       return new MissingDetailResponse(
                userMember.getUserName(),
                userMember.getPhoneNumber(),
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
                        .toList()
        );


    }

    // 실종글 삭제
    public void deleteMissingReport(Long missingReportId, CustomUserDetails user) {
        MissingReportEntity missingReport = missingReportRepository.findById(missingReportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MISSING_REPORT));

        if (!missingReport.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_BOARD);
        }

        missingReport.setDeleted(true);
    }

    // 실종 상태 변경
    public void changeMissingStatus(ChangeMissingStatusRecord request, CustomUserDetails user) {

        MissingReportEntity missingReport = missingReportRepository.findById(request.missingReportId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MISSING_REPORT));

        if (!missingReport.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_BOARD);
        }

        missingReport.setStatus(request.status());
    }
}
