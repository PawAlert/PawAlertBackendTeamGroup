package com.pawalert.backend.domain.volunteer.repository;

import com.pawalert.backend.domain.volunteer.entity.VolunteerActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerActivityRepository extends JpaRepository<VolunteerActivity, Long>, VolunteerActivityRepositoryCustom {

}