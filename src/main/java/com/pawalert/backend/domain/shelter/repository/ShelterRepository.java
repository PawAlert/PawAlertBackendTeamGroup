package com.pawalert.backend.domain.shelter.repository;

import com.pawalert.backend.domain.shelter.entity.AnimalRescueOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShelterRepository extends JpaRepository<AnimalRescueOrganizationEntity, Long> {
    Boolean existsByUserId(Long id);
    Optional<AnimalRescueOrganizationEntity> findByUserId(Long id);

}
