package com.pawalert.backend.domain.mypet.service;


import com.pawalert.backend.domain.mypet.entity.PetEntity;
import com.pawalert.backend.domain.mypet.entity.PetImageEntity;
import com.pawalert.backend.domain.mypet.model.PetGetResponse;
import com.pawalert.backend.domain.mypet.model.PetImageListRecord;
import com.pawalert.backend.domain.mypet.model.PetRegisterRequest;
import com.pawalert.backend.domain.mypet.model.PetUpdateRequest;
import com.pawalert.backend.domain.mypet.repository.PetImageRepository;
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
    private final PetImageRepository petImageRepository;

    @Transactional
    public void createMyPet(PetRegisterRequest request, CustomUserDetails user, List<MultipartFile> images) {

        UserEntity userMember = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.FORBIDDEN_ERROR));

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
                .build();

        // 이미지 저장
        List<PetImageEntity> petImage = images.stream()
                .map(image -> {
                    String imageUrl = saveImage.SaveImages(image);
                    return PetImageEntity.builder()
                            .pet(pet)
                            .photoUrl(imageUrl)
                            .isDeleted(false)
                            .build();
                }).toList();

        petRepository.save(pet);
        petImageRepository.saveAll(petImage);

    }

    //펫 정보 수정
    @Transactional
    public void updateMyPet(PetUpdateRequest request, CustomUserDetails user, List<MultipartFile> images) {
        PetEntity pet = petRepository.findById(request.petId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_PET_ERROR));

        // 삭제 처리 false -> true
        if (!request.deletePhotoIds().isEmpty()) {
            List<PetImageEntity> petImages = pet.getPetImages();
            for (Long deletePhotoId : request.deletePhotoIds()) {
                petImages.stream()
                        .filter(image -> image.getId().equals(deletePhotoId))
                        .forEach(image -> {
                            image.setDeleted(true);
                        });
            }
        }

        // 이미지 저장
        List<PetImageEntity> newPetImage = images.stream()
                .map(image -> {
                    String petImageUrl = saveImage.SaveImages(image);
                    return PetImageEntity.builder()
                            .pet(pet)
                            .isDeleted(false)
                            .photoUrl(petImageUrl)
                            .build();
                }).toList();
        pet.getPetImages().addAll(newPetImage);

        pet.setPetName(request.petName());
        pet.setPetName(request.petName());
        pet.setSpecies(request.species());
        pet.setBreed(request.breed());
        pet.setColor(request.color());
        pet.setGender(request.gender());
        pet.setMicrochipId(request.microchipId());
        pet.setNeutering(request.neutering());
        pet.setAge(request.age());

        petRepository.save(pet);

    }

    //펫 정보 조회
    @Transactional
    public PetGetResponse getMyPet(Long petId, CustomUserDetails user) {
        PetEntity pet = petRepository.findById(petId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_PET_ERROR));

        List<PetImageListRecord> petImageRecords = pet.getPetImages().stream()
                .filter(image -> !image.isDeleted())
                .map(image -> new PetImageListRecord(image.getId(), image.getPhotoUrl()))
                .toList();


        return new PetGetResponse(
                pet.getId(),
                pet.getPetName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getColor(),
                pet.getGender(),
                pet.getMicrochipId(),
                pet.isNeutering(),
                pet.getAge(),
                petImageRecords
                );
    }

    //펫 정보 삭제
    @Transactional
    public void deleteMyPet(Long petId, CustomUserDetails user) {
        PetEntity pet = petRepository.findById(petId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_PET_ERROR));

        pet.setDeleted(true);
        petRepository.save(pet);
    }


}
