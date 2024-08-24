package com.pawalert.backend.domain.mypet.repository;

import com.pawalert.backend.domain.mypet.entity.PetImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetImageRepository extends JpaRepository<PetImageEntity, Long> {
}
