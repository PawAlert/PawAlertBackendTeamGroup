package com.pawalert.backend.domain.official.service;

import com.pawalert.backend.domain.official.entity.OfficialEntity;
import com.pawalert.backend.domain.official.model.OfficialRegistrationDto;
import com.pawalert.backend.domain.official.model.OfficialResponseDto;
import com.pawalert.backend.domain.official.repository.OfficialRepository;
import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.model.UserRole;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.Location;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pawalert.backend.global.httpstatus.exception.ErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
public class OfficialService {

    private final OfficialRepository officialRepository;
    private final UserRepository userRepository;

    @Transactional
    public String registerOfficial(String userUid, OfficialRegistrationDto dto) {
        UserEntity user = userRepository.findByUid(userUid).orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

        Location location = Location.from(dto.location());
        OfficialEntity official = dto.toEntity(userUid, OfficialEntity.ApprovalStatus.PENDING, location);
        user.setRole(UserRole.ROLE_OFFICIAL_USER);
        userRepository.save(user);
        OfficialEntity savedOfficial = officialRepository.save(official);
        return savedOfficial.getEmail();
    }

    @Transactional
    //todo : 어떤 항목 업데이트할지
    public String updateOfficial(String userUid, OfficialRegistrationDto dto) {
        OfficialEntity official = officialRepository.findByUserUid(userUid)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

        return official.getEmail();
    }

    @Transactional(readOnly = true)
    public OfficialResponseDto getOfficial(String userUid) {
        OfficialEntity official = officialRepository.findByUserUid(userUid)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

        return new OfficialResponseDto(
                official.getInstitutionName(),
                official.getRepresentativeName(),
                official.getEmail(),
                official.getPhoneNumber(),
                official.getInstitutionType(),
                official.getLocation(),
                official.getWebsite(),
                official.getInstitutionDescription(),
                official.getOperatingHours(),
                official.getStatus().toString()
        );
    }
}