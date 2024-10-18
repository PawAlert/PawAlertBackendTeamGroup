package com.pawalert.backend.domain.volunteer.repository;

import com.pawalert.backend.domain.volunteer.dto.VolunteerActivitySearchCondition;
import com.pawalert.backend.domain.volunteer.entity.VolunteerActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VolunteerActivityRepositoryCustom {
    Page<VolunteerActivity> searchVolunteerActivities(VolunteerActivitySearchCondition condition, Pageable pageable);
}