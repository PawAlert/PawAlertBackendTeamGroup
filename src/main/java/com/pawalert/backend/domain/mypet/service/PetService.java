package com.pawalert.backend.domain.mypet.service;


import com.pawalert.backend.domain.mypet.entity.PetEntity;
import com.pawalert.backend.domain.mypet.model.PetRegisterRequest;
import com.pawalert.backend.domain.mypet.repository.PetRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.repository.UserRepository;
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
public class PetService {
    private final UserRepository userRepository;
    private final SaveImage saveImage;
    private final PetRepository petRepository;

    @Transactional
    public void createMyPet(PetRegisterRequest request, CustomUserDetails user, List<MultipartFile> images) {

        UserEntity userMember = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.FORBIDDEN_ERROR));

        // 이미지 저장
        List<String> imageUrls = images.stream()
                .map(image -> {
                    try {
                        return saveImage.SaveImages(image);
                    } catch (Exception e) {
                        throw new BusinessException(ErrorCode.FAIL_FILE_UPLOAD);
                    }
                }).toList();

        PetEntity pet = PetEntity.builder()
                .user(userMember)
                .petName(request.petName())
                .species(request.species())
                .breed(request.breed())
                //boolean
                .neutering(request.neutering())
                .color(request.color())
                .age(request.age())
                .gender(request.gender())
                .microchipId(request.microchipId())
                .photoUrls(imageUrls)
                .build();

        petRepository.save(pet);

    }
}
