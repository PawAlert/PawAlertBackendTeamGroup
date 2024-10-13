package com.pawalert.backend.domain.missing.repository;

import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.domain.missing.model.MissingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissingReportRepository extends JpaRepository<MissingReportEntity, Long>, CustomMissingRepository {
    Page<MissingReportEntity> findByDeletedFalseAndStatus(MissingStatus status, Pageable pageable);
}
