package com.pawalert.backend.domain.missing.repository;

import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.domain.missing.model.MissingStatus;
import com.pawalert.backend.domain.missing.model.MissingViewListRequest;
import com.pawalert.backend.domain.missing.model.MissingViewListResponse;
import com.pawalert.backend.global.LocataionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MissingReportRepositoryCustom {
    Page<MissingViewListResponse> searchMissingReports(MissingViewListRequest request, Pageable pageable);
}
