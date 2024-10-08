package com.pawalert.backend.domain.mypet.service;


import com.pawalert.backend.domain.mypet.entity.PetEntity;
import com.pawalert.backend.domain.mypet.entity.PetImageEntity;
import com.pawalert.backend.domain.mypet.model.*;
import com.pawalert.backend.domain.mypet.repository.PetImageRepository;
import com.pawalert.backend.domain.mypet.repository.PetRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.aws.SaveImage;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SuccessResponse<String>> createMyPet(PetRegisterRequest request, CustomUserDetails user, List<MultipartFile> images) {

        UserEntity userMember = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        PetEntity pet = PetEntity.builder()
                .user(userMember)
                .petName(request.petName())
                .species(request.species())
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

        return ResponseHandler.generateResponse(HttpStatus.CREATED, "펫 등록 성공", "반려동물 이름 : " + pet.getPetName());

    }

    //펫 정보 수정
    @Transactional
    public ResponseEntity<SuccessResponse<String>> updateMyPet(PetUpdateRequest request, CustomUserDetails user, List<MultipartFile> images) {
        // 펫 조회
        PetEntity pet = petRepository.findById(request.petId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PET));

        // 사용자가 소유한 펫인지 확인
        if (!pet.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 삭제 처리 false -> true
        if (!request.deletePhotoIds().isEmpty()) {
            List<PetImageEntity> petImages = pet.getPetImages();
            for (Long deletePhotoId : request.deletePhotoIds()) {
                PetImageEntity imageToDelete = petImages.stream()
                        .filter(image -> image.getId().equals(deletePhotoId))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException(ErrorCode.FORBIDDEN));

                // 이미지 소유권 확인
                if (!imageToDelete.getPet().getId().equals(pet.getId())) {
                    throw new BusinessException(ErrorCode.FORBIDDEN);
                }

                imageToDelete.setDeleted(true);
            }
        }

        // 이미지 저장
        List<PetImageEntity> newPetImages = images.stream()
                .map(image -> {
                    String petImageUrl = saveImage.SaveImages(image);
                    return PetImageEntity.builder()
                            .pet(pet)
                            .isDeleted(false)
                            .photoUrl(petImageUrl)
                            .build();
                }).toList();

        pet.getPetImages().addAll(newPetImages);

        // 펫 정보 업데이트
        pet.setPetName(request.petName());
        pet.setSpecies(request.species());
        pet.setColor(request.color());
        pet.setGender(request.gender());
        pet.setMicrochipId(request.microchipId());
        pet.setNeutering(request.neutering());
        pet.setAge(request.age());

        return ResponseHandler.generateResponse(HttpStatus.OK, "펫 정보 수정 성공", "반려동물 이름 : " + pet.getPetName());
    }


    //펫 정보 조회
    @Transactional
    public ResponseEntity<?> getPets(CustomUserDetails user) {
        UserEntity userMember = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        List<PetEntity> pets = petRepository.findAllById(userMember.getPets().stream()
                .map(PetEntity::getId)
                .toList());

        List<PetGetResponse> petResponses = pets.stream()
                .map(pet -> {
                    List<PetImageListRecord> petImageRecords = pet.getPetImages().stream()
                            .filter(image -> !image.isDeleted())
                            .map(image -> new PetImageListRecord(image.getId(), image.getPhotoUrl()))
                            .toList();

                    return new PetGetResponse(
                            pet.getId(),
                            pet.getPetName(),
                            pet.getSpecies(),
                            pet.getColor(),
                            pet.getGender(),
                            pet.getMicrochipId(),
                            pet.isNeutering(),
                            pet.getAge(),
                            petImageRecords
                    );
                })
                .toList();

        return ResponseHandler.generateResponse(HttpStatus.OK, "펫 정보 조회 성공", petResponses);
    }


    //펫 정보 삭제
    @Transactional
    public ResponseEntity<String> deleteMyPet(Long petId, CustomUserDetails user) {
        try{
            PetEntity pet = petRepository.findById(petId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PET));
            pet.setDeleted(true);
            // 관련 이미지 처리 (삭제 마킹)
            List<PetImageEntity> images = pet.getPetImages();
            for (PetImageEntity image : images) {
                image.setDeleted(true);
            }
        } catch (BusinessException e) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return ResponseEntity.noContent().build();

    }


}
