package com.pawalert.backend.domain.missing.repository;

import com.pawalert.backend.domain.missing.entity.QMissingReportEntity;
import com.pawalert.backend.domain.missing.entity.QMissingReportImageEntity;
import com.pawalert.backend.domain.missing.model.MissingStatus;
import com.pawalert.backend.domain.missing.model.MissingViewListRequest;
import com.pawalert.backend.domain.missing.model.MissingViewListResponse;
import com.pawalert.backend.global.LocataionRecord;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.media.Schema;
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
            builder.and(missingReport.location.addressName.eq(request.address()));
        }
        if (!request.addressDetail1().isEmpty()) {
            builder.and(missingReport.location.addressDetail1.eq(request.addressDetail1()));
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
                        missingReport.rewardAmount,
                        missingReport.content,
                        missingReport.user.phoneNumber

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
