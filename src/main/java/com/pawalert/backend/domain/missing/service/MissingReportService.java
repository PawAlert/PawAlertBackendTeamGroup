package com.pawalert.backend.domain.missing.service;


import com.pawalert.backend.domain.comment.dto.CommentResponse;
import com.pawalert.backend.domain.comment.entity.CommentEntity;
import com.pawalert.backend.domain.comment.repository.CommentRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
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
    private final CommentRepository commentRepository;

    // 실종글 수정
    @Transactional
    public ResponseEntity<SuccessResponse<String>> updateMissingReport(
            MissingUpdateRequest request,
            CustomUserDetails user) {

        // 사용자 정보를 가져옵니다.
        UserEntity userMember = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        // 게시글 정보를 가져옵니다.
        MissingReportEntity missingReport = missingReportRepository.findById(request.missingReportId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MISSING_REPORT));


        // 게시글 작성자가 현재 사용자와 일치하는지 확인합니다.
        if (!missingReport.getUser().getId().equals(userMember.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 게시글 정보를 업데이트합니다.
        missingReport.setTitle(request.title());
        missingReport.setDescription(request.petDescription());
        missingReport.setContact1(request.contact1());
        missingReport.setContact2(request.contact2());
        missingReport.getPet().setSpecies(request.petSpecies());
        missingReport.getPet().setMicrochipId(request.microchipId());
        missingReport.getPet().setDescription(request.description());
        missingReport.setStatus(MissingStatus.valueOf(request.missingStatus()));


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
            // 위치 저장
            Location location = Location.from(request.locataionRecord());
            // 반려동물 정보 저장
            PetEntity pet = PetEntity.fromRequest(request, userMember);
            petRepository.save(pet);

            // 반려동물 실종 정보 저장
            MissingReportEntity missingReport = MissingReportEntity.fromRequest(request, location, userMember, pet);
            missingReportRepository.save(missingReport);

            List<MissingReportImageEntity> imageUrls = images.stream()
                    .map(image -> MissingReportImageEntity.from(saveImage.SaveImages(image), missingReport))
                    .toList();
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

          // 댓글 따로 만들었음,
//        List<CommentResponse> commentResponses = comments.stream()
//                .map(comment -> {
//                    // 내가 작성한 댓글인지
//                    boolean isCommentMine = Optional.ofNullable(user).isPresent() &&
//                            comment.getUserId().equals(user.getUid());
//                    return new CommentResponse(
//                            comment.getId(),
//                            comment.getUserId(),
//                            comment.getMissingReportId(),
//                            comment.getContent(),
//                            comment.isDeleted(),
//                            comment.getTimestamp(),
//                            isCommentMine
//                    );
//                }).toList();


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
                missingReport.getContact1(),
                missingReport.getContact2()
        );
        return ResponseHandler.generateResponse(HttpStatus.OK, "Missing report detail retrieved successfully", response);

    }

    // 실종글 리스트 조회
    public Page<MissingViewListResponse> getMissingReports(Pageable pageable) {
        Page<MissingReportEntity> reportsPage = missingReportRepository.findAll(pageable);

        // List로 변환한 후 필터링
        List<MissingViewListResponse> filteredResponseList = reportsPage.getContent().stream()
                .filter(missingReport -> !missingReport.isDeleted()) // deleted = false인 항목만 필터링
                .map(missingReport -> {
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
                            missingReport.getDescription(),
                            missingReport.getContact1()
                    );
                }).toList();

        return new PageImpl<>(filteredResponseList, pageable, reportsPage.getTotalElements());
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


}
