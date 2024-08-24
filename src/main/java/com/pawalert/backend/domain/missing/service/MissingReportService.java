package com.pawalert.backend.domain.missing.service;


import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.domain.missing.entity.MissingReportImageEntity;
import com.pawalert.backend.domain.missing.model.MissingReportRecord;
import com.pawalert.backend.domain.missing.repository.MissingImageRepository;
import com.pawalert.backend.domain.missing.repository.MissingReportRepository;
import com.pawalert.backend.domain.mypet.entity.PetEntity;
import com.pawalert.backend.domain.mypet.entity.PetImageEntity;
import com.pawalert.backend.domain.mypet.repository.PetImageRepository;
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

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissingReportService {
    private final UserRepository userRepository;
    private final MissingReportRepository missingReportRepository;
    private final SaveImage saveImage;
    private final PetRepository petRepository;
    private final MissingImageRepository missingImageRepository;


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
                .rewardAmount(BigDecimal.valueOf(request.rewardAmount()))
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

}
