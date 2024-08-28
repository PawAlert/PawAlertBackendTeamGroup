package com.pawalert.backend.domain.missing.repository;

import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingReportRepository extends JpaRepository<MissingReportEntity, Long>, MissingReportRepositoryCustom {
}
