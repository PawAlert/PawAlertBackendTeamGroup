package com.pawalert.backend.domain.volunteer.repository;

import com.pawalert.backend.domain.volunteer.dto.VolunteerActivitySearchCondition;
import com.pawalert.backend.domain.volunteer.entity.ActivityType;
import com.pawalert.backend.domain.volunteer.entity.VolunteerActivity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.List;

import static com.pawalert.backend.domain.volunteer.entity.QVolunteerActivity.volunteerActivity;

public class VolunteerActivityRepositoryImpl implements VolunteerActivityRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public VolunteerActivityRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<VolunteerActivity> searchVolunteerActivities(VolunteerActivitySearchCondition condition, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (condition.activityType() != null) {
            builder.and(volunteerActivity.activityType.eq(condition.activityType()));
        }
        if (condition.province() != null && !condition.province().isEmpty()) {
            builder.and(volunteerActivity.location.province.eq(condition.province()));
        }
        if (condition.city() != null && !condition.city().isEmpty()) {
            builder.and(volunteerActivity.location.city.eq(condition.city()));
        }

        List<VolunteerActivity> content = queryFactory
                .selectFrom(volunteerActivity)
                .where(builder)
                .orderBy(condition.sortByClosest() ? volunteerActivity.date.asc() : volunteerActivity.date.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(volunteerActivity.count())
                .from(volunteerActivity)
                .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression activityTypeEq(ActivityType activityType) {
        return activityType != null ? volunteerActivity.activityType.eq(activityType) : null;
    }

    private BooleanExpression provinceEq(String province) {
        return province != null ? volunteerActivity.location.province.eq(province) : null;
    }

    private BooleanExpression cityEq(String city) {
        return city != null ? volunteerActivity.location.city.eq(city) : null;
    }
}