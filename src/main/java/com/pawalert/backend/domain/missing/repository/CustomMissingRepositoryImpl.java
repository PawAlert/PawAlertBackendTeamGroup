package com.pawalert.backend.domain.missing.repository;

import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.domain.missing.entity.QMissingReportEntity;
import com.pawalert.backend.domain.missing.model.MissingViewListResponse;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomMissingRepositoryImpl implements CustomMissingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    // 내가 작성한 글
    @Override
    public List<MissingViewListResponse> findEqualMissingReportsId(Long userId) {
        QMissingReportEntity missingReportEntity = QMissingReportEntity.missingReportEntity;

        // 작성자의 ID로 글 필터링
        List<MissingReportEntity> missingReports = queryFactory.selectFrom(missingReportEntity)
                .where(missingReportEntity.user.id.eq(userId)) // 작성자의 ID와 일치하는 글 조회
                .fetch();

        return MissingViewListResponse.fromList(missingReports);
    }
}
