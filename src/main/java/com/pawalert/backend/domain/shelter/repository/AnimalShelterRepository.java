package com.pawalert.backend.domain.shelter.repository;

import com.pawalert.backend.domain.shelter.entity.AnimalShelterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalShelterRepository extends JpaRepository<AnimalShelterEntity, Long> {
    //관할구역 + 보호센터명
    AnimalShelterEntity findByJurisdictionAndShelterName(String jurisdiction, String shelterName);
}
