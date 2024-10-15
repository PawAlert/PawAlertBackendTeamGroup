package com.pawalert.backend.domain.inquiry.repository;

import com.pawalert.backend.domain.inquiry.entity.InquiryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<InquiryEntity, Long> {
    List<InquiryEntity> findByUserUidOrderByCreatedAtDesc(String userUid);
    Page<InquiryEntity> findAll(Pageable pageable);
}