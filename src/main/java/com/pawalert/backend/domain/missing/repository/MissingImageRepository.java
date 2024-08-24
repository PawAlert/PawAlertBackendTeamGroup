package com.pawalert.backend.domain.missing.repository;

import com.pawalert.backend.domain.missing.entity.MissingReportImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingImageRepository extends JpaRepository<MissingReportImageEntity, Long> {
}
