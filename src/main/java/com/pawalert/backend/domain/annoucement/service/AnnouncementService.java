package com.pawalert.backend.domain.annoucement.service;

import com.pawalert.backend.domain.annoucement.entity.AnnouncementEntity;
import com.pawalert.backend.domain.annoucement.model.AnnouncementDetailDto;
import com.pawalert.backend.domain.annoucement.model.AnnouncementDto;
import com.pawalert.backend.domain.annoucement.model.AnnouncementSummaryDto;
import com.pawalert.backend.domain.annoucement.repository.AnnouncementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Transactional
    public String createAnnouncement(String officialId, AnnouncementDto dto) {
        AnnouncementEntity announcement = dto.toEntity(officialId);
        AnnouncementEntity savedAnnouncement = announcementRepository.save(announcement);
        return savedAnnouncement.getTitle();
    }

    @Transactional(readOnly = true)
    public Page<AnnouncementSummaryDto> getAnnouncementSummaries(Pageable pageable) {
        Page<AnnouncementEntity> announcementPage = announcementRepository.findAll(pageable);
        return announcementPage.map(AnnouncementSummaryDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public AnnouncementDetailDto getAnnouncementDetail(Long id) {
        AnnouncementEntity announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 공고를 찾을 수 없습니다: " + id));
        return AnnouncementDetailDto.fromEntity(announcement);
    }

}