package com.pawalert.backend.domain.organization.repository;

import com.pawalert.backend.domain.organization.entity.AnimalRescueOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//todo : entity 변수명 바꾸자 너무 헷갈림
public interface ShelterRepository extends JpaRepository<AnimalRescueOrganizationEntity, Long> {

}
