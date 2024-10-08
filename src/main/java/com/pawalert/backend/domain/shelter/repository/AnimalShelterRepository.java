package com.pawalert.backend.domain.shelter.repository;

import com.pawalert.backend.domain.shelter.entity.AnimalShelterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalShelterRepository extends JpaRepository<AnimalShelterEntity, Long> {
    //관할구역 + 보호센터명
    Optional<AnimalShelterEntity> findByJurisdictionAndShelterName(String jurisdiction, String shelterName);
}
