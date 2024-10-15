package com.pawalert.backend.domain.annoucement.repository;

import com.pawalert.backend.domain.annoucement.entity.AnnouncementEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {
    Page<AnnouncementEntity> findByOfficialId(String officialId, Pageable pageable);
}
