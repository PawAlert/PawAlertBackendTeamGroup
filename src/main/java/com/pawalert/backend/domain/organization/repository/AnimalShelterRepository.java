package com.pawalert.backend.domain.organization.repository;

import com.pawalert.backend.domain.organization.entity.AnimalShelter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalShelterRepository extends JpaRepository<AnimalShelter, Long> {
    //관할구역 + 보호센터명
    AnimalShelter findByJurisdictionAndShelterName(String jurisdiction, String shelterName);
}
