package com.pawalert.backend.domain.volunteer.service;

import com.pawalert.backend.domain.volunteer.dto.VolunteerActivityDetailDto;
import com.pawalert.backend.domain.volunteer.dto.VolunteerActivityRequest;
import com.pawalert.backend.domain.volunteer.dto.VolunteerActivitySearchCondition;
import com.pawalert.backend.domain.volunteer.entity.VolunteerActivity;
import com.pawalert.backend.domain.volunteer.repository.VolunteerActivityRepository;
import com.pawalert.backend.global.Location;
import com.pawalert.backend.global.httpstatus.exception.BusinessException;
import com.pawalert.backend.global.httpstatus.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VolunteerActivityService {

    private final VolunteerActivityRepository volunteerActivityRepository;

    @Transactional
    public String createVolunteerActivity(String userUid, VolunteerActivityRequest request) {
        VolunteerActivity activity = VolunteerActivity.builder()
                .userUid(userUid)
                .title(request.title())
                .description(request.description())
                .date(request.date())
                .activityType(request.activityType())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .location(Location.from(request.location()))
                .requiredVolunteers(request.requiredVolunteers())
                .requiredSkills(request.requiredSkills())
                .organizationName(request.organizationName())
                .contactName(request.contactName())
                .contactEmail(request.contactEmail())
                .contactPhone(request.contactPhone())
                .images(request.images())
                .build();

        VolunteerActivity savedActivity = volunteerActivityRepository.save(activity);
        return savedActivity.getTitle();
    }

    @Transactional(readOnly = true)
    public VolunteerActivityDetailDto getVolunteerActivityDetail(Long id) {
        VolunteerActivity activity = volunteerActivityRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_VOLUNTEER_ACTIVITY));
        return VolunteerActivityDetailDto.from(activity);
    }

    @Transactional(readOnly = true)
    public Page<VolunteerActivityDetailDto> searchVolunteerActivities(VolunteerActivitySearchCondition condition, Pageable pageable) {
        Page<VolunteerActivity> activities = volunteerActivityRepository.searchVolunteerActivities(condition, pageable);
        return activities.map(VolunteerActivityDetailDto::from);
    }
}