package com.pawalert.backend.domain.missing.repository;

import com.pawalert.backend.domain.missing.entity.QMissingReportEntity;
import com.pawalert.backend.domain.missing.entity.QMissingReportImageEntity;
import com.pawalert.backend.domain.missing.model.MissingStatus;
import com.pawalert.backend.domain.missing.model.MissingViewListRequest;
import com.pawalert.backend.domain.missing.model.MissingViewListResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class MissingReportRepositoryCustomImpl implements MissingReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MissingViewListResponse> searchMissingReports(MissingViewListRequest request, Pageable pageable) {
        QMissingReportEntity missingReport = QMissingReportEntity.missingReportEntity;
        QMissingReportImageEntity missingPetImage = QMissingReportImageEntity.missingReportImageEntity;

        BooleanBuilder builder = new BooleanBuilder();

        // 상태 필터
        if (!request.status().isEmpty()) {
            builder.and(missingReport.status.eq(MissingStatus.valueOf(request.status())));
        }

        // 주소 정보 조건 추가
        if (!request.address().isEmpty()) {
            builder.and(missingReport.location.address.eq(request.address()));
        }
        if (!request.addressDetail().isEmpty()) {
            builder.and(missingReport.location.addressDetail.eq(request.addressDetail()));
        }


        // Querydsl 쿼리 실행 및 결과 매핑
        List<MissingViewListResponse> results = queryFactory
                .select(Projections.constructor(
                        MissingViewListResponse.class,
                        missingReport.id, // 실종글 Id
                        missingReport.user.id.stringValue(), // 작성자 ID
                        missingReport.user.email, // 작성자 email
                        missingReport.title, // 제목
                        missingReport.dateLost, // 실종날짜
                        missingReport.location.postcode, // 위치코드
                        missingReport.location.address, // 주소
                        missingReport.location.addressDetail, // 상세주소
                        missingReport.status.stringValue(), // 실종상태
                        missingReport.pet.petName, // 펫 이름
                        missingReport.pet.species, // 펫 품종
                        missingReport.pet.color, // 펫 색상
                        missingReport.pet.age, // 펫 나이
                        missingReport.pet.gender, // 펫 성별
                        missingPetImage.missingPhotoUrl.coalesce(""), // 첫 번째 펫 이미지 URL
                        missingReport.content, // 설명
                        missingReport.user.phoneNumber // 연락처

                ))
                .from(missingReport)
                .leftJoin(missingReport.missingPetImages, missingPetImage)
                .where(builder)
                .groupBy(missingReport.id)
                .orderBy(missingReport.dateLost.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 가져오기
        long total = queryFactory
                .selectFrom(missingReport)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}

