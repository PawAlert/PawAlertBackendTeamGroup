package com.pawalert.backend.domain.missing.repository;

import com.pawalert.backend.domain.missing.model.MissingViewListResponse;

import java.util.List;

public interface CustomMissingRepository {
    // 실종 신고 데이터를 커스텀 쿼리로 조회하는 메서드 예시
    List<MissingViewListResponse> findEqualMissingReportsId(Long userId);
}
