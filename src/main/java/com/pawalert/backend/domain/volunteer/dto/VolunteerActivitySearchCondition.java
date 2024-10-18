package com.pawalert.backend.domain.volunteer.dto;

import com.pawalert.backend.domain.volunteer.entity.ActivityType;

public record VolunteerActivitySearchCondition(
        ActivityType activityType,
        String province,
        String city,
        boolean sortByClosest
) {
    // 기본 생성자를 제공하여 @ModelAttribute가 제대로 동작
    public VolunteerActivitySearchCondition() {
        this(null, null, null, true);
    }

    // sortByClosest의 기본값을 true로 설정하는 생성자
    public VolunteerActivitySearchCondition(ActivityType activityType, String province, String city) {
        this(activityType, province, city, true);
    }
}