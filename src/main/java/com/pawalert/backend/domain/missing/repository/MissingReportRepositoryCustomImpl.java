package com.pawalert.backend.domain.missing.repository;

import com.pawalert.backend.domain.missing.entity.QMissingReportEntity;
import com.pawalert.backend.domain.missing.entity.QMissingReportImageEntity;
import com.pawalert.backend.domain.missing.model.MissingStatus;
import com.pawalert.backend.domain.missing.model.MissingViewListResponse;
import com.pawalert.backend.global.LocataionRecord;
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
    public Page<MissingViewListResponse> searchMissingReports(String title, String status, LocataionRecord location, Pageable pageable) {
        QMissingReportEntity missingReport = QMissingReportEntity.missingReportEntity;
        QMissingReportImageEntity missingPetImage = QMissingReportImageEntity.missingReportImageEntity;
        BooleanBuilder builder = new BooleanBuilder();

        // 상태 필터
        if (status != null) {
            builder.and(missingReport.status.eq(MissingStatus.valueOf(status)));
        }

        // 주소 정보 조건 추가
        if (location != null) {
            if (location.addressName() != null && !location.addressName().isEmpty()) {
                builder.and(missingReport.location.addressName.eq(location.addressName()));
            }
            if (location.addressDetail1() != null && !location.addressDetail1().isEmpty()) {
                builder.and(missingReport.location.addressDetail1.eq(location.addressDetail1()));
            }
        }

        // Querydsl 쿼리 실행 및 결과 매핑
        List<MissingViewListResponse> results = queryFactory
                .select(Projections.constructor(
                        MissingViewListResponse.class,
                        missingReport.id,
                        missingReport.user.id.stringValue(),
                        missingReport.user.email,
                        missingReport.title,
                        missingReport.dateLost,
                        missingReport.location.addressName,
                        missingReport.location.addressDetail1,
                        missingReport.status.stringValue(),
                        missingReport.pet.petName,
                        missingReport.pet.species,
                        missingReport.pet.color,
                        missingReport.pet.age,
                        missingReport.pet.gender,
                        // 첫 번째 이미지 URL만 가져오기
                        missingPetImage.missingPhotoUrl.coalesce(""),
                        missingReport.rewardAmount
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
