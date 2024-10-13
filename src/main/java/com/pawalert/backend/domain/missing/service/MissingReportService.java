package com.pawalert.backend.domain.missing.service;


import com.pawalert.backend.domain.comment.repository.CommentRepository;
import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.domain.missing.entity.MissingReportImageEntity;
import com.pawalert.backend.domain.missing.model.*;
import com.pawalert.backend.domain.missing.repository.MissingImageRepository;
import com.pawalert.backend.domain.missing.repository.MissingReportRepository;
import com.pawalert.backend.domain.mypet.entity.PetEntity;
import com.pawalert.backend.domain.mypet.repository.PetRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.Location;
import com.pawalert.backend.global.aws.S3Service;
import com.pawalert.backend.global.aws.SaveImage;
import com.pawalert.backend.global.config.redis.RedisService;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import com.pawalert.backend.global.httpstatus.exception.ResponseHandler;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    private final RedisService redisService;
    private final S3Service s3Service;


    // 실종글 수정
    @Transactional
    public ResponseEntity<SuccessResponse<String>> updateMissingReport(
            MissingPatchRequest request,
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
        missingReport.setMissingTitle(request.title());
        // 실종 당시 설명
        missingReport.setIncidentDescription(request.petDescription());
        missingReport.setContact1(request.contact1());
        missingReport.setContact2(request.contact2());
        missingReport.setStatus(MissingStatus.valueOf(request.missingStatus()));


        // 응답 생성
        return ResponseHandler.generateResponse(HttpStatus.OK, "Missing report updated successfully", userMember.getEmail());
    }


    // 실종글 등록
    @Transactional
    public ResponseEntity<SuccessResponse<List<String>>> createMissingReport(MissingReportRecord request,
                                                                             CustomUserDetails user) {
        UserEntity userMember = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        try {
            // 위치 저장
            Location location = Location.from(request.locationRecord());

            // 반려동물 실종 게시글 생성 (이미지 포함)
            MissingReportEntity missingReport = MissingReportEntity.fromRequest(request, location, userMember);

            // 이미지 엔티티 생성 및 설정
            List<MissingReportImageEntity> imageEntities = request.missingPetImages().stream()
                    .map(imageUrl -> MissingReportImageEntity.from(imageUrl, missingReport))
                    .collect(Collectors.toList());

            missingReport.setMissingPetImages(imageEntities);

            missingReportRepository.save(missingReport);

            List<String> data = List.of(
                    "user Email = " + userMember.getEmail(),
                    "pet name = " + request.missingPetName()
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

        MissingDetailResponse response = MissingDetailResponse.from(missingReport, isMine);

        return ResponseHandler.generateResponse(HttpStatus.OK, "Missing report detail retrieved successfully", response);

    }

    // 실종글 리스트 조회
    public Page<MissingViewListResponse> getMissingReports(Pageable pageable, String sortDirection, String statusFilter) {
        // 모든 데이터 조회
        List<MissingReportEntity> allReports = missingReportRepository.findAll(); // 먼저 모든 데이터를 가져옴

        // 먼저 필터링
        List<MissingReportEntity> filteredReports = allReports.stream()
                .filter(missingReport -> !missingReport.isDeleted()) // deleted = false인 항목만 필터링
                .filter(missingReport -> missingReport.getStatus().name().equalsIgnoreCase(statusFilter)) // 상태 필터 적용
                .toList();

        // 필터링된 데이터를 정렬
        Comparator<MissingReportEntity> comparator = Comparator.comparing(MissingReportEntity::getDateLost);
        if ("DESC".equals(sortDirection)) {
            comparator = comparator.reversed(); // DESC일 경우 역순 정렬
        }
        List<MissingReportEntity> sortedFilteredReports = filteredReports.stream()
                .sorted(comparator) // 필터링된 데이터를 정렬
                .toList();

        // 필터링된 리스트를 페이지로 변환 후 반환
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sortedFilteredReports.size());
        List<MissingViewListResponse> pageContent = sortedFilteredReports.subList(start, end).stream()
                .map(MissingViewListResponse::from) // 필터링된 데이터를 DTO로 변환
                .toList();

        // 최종 결과 반환
        return new PageImpl<>(pageContent, pageable, sortedFilteredReports.size());
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
