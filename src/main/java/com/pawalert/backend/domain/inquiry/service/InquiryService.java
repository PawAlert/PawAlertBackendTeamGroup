package com.pawalert.backend.domain.inquiry.service;

import com.pawalert.backend.domain.inquiry.dto.InquiryDto;
import com.pawalert.backend.domain.inquiry.entity.InquiryEntity;
import com.pawalert.backend.domain.inquiry.repository.InquiryRepository;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    @Transactional
    public Long createInquiry(InquiryDto dto, CustomUserDetails user) {
        InquiryEntity inquiry = dto.toEntity(user.getUid());
        InquiryEntity savedInquiry = inquiryRepository.save(inquiry);
        return savedInquiry.getId();
    }

    @Transactional(readOnly = true)
    public Page<InquiryDto> getAllInquiries(Pageable pageable) {
        Page<InquiryEntity> inquiryPage = inquiryRepository.findAll(pageable);
        return inquiryPage.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public InquiryDto getInquiryDetail(Long id) {
        InquiryEntity inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 문의를 찾을 수 없습니다: " + id));
        return convertToDto(inquiry);
    }

    @Transactional(readOnly = true)
    public List<InquiryDto> getUserInquiries(String userUid) {
        List<InquiryEntity> inquiries = inquiryRepository.findByUserUidOrderByCreatedAtDesc(userUid);
        return inquiries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private InquiryDto convertToDto(InquiryEntity entity) {
        return InquiryDto.builder()
                .id(entity.getId())
                .type(entity.getType())
                .name(entity.getName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .content(entity.getContent())
                .build();
    }
}