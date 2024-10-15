package com.pawalert.backend.domain.Notice.repository;

import com.pawalert.backend.domain.Notice.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    Page<NoticeEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}