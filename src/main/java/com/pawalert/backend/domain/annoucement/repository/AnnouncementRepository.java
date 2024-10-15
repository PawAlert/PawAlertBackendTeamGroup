package com.pawalert.backend.domain.annoucement.repository;

import com.pawalert.backend.domain.annoucement.entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {
}
