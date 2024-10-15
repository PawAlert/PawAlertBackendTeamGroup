package com.pawalert.backend.domain.Notice.service;

import com.pawalert.backend.domain.Notice.entity.NoticeEntity;
import com.pawalert.backend.domain.Notice.model.NoticeDto;
import com.pawalert.backend.domain.Notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public String createNotice(String authorId, NoticeDto dto) {
        NoticeEntity notice = dto.toEntity(authorId);
        NoticeEntity savedNotice = noticeRepository.save(notice);
        return savedNotice.getTitle();
    }

    @Transactional(readOnly = true)
    public Page<NoticeDto> getAllNotices(Pageable pageable) {
        Page<NoticeEntity> noticePage = noticeRepository.findAllByOrderByCreatedAtDesc(pageable);
        return noticePage.map(this::convertToDto);
    }

    private NoticeDto convertToDto(NoticeEntity entity) {
        return NoticeDto.builder()
                .title(entity.getTitle())
                .content(entity.getContent())
                .type(entity.getType())
                .validFrom(entity.getValidFrom())
                .validUntil(entity.getValidUntil())
                .priority(entity.getPriority())
                .build();
    }
}