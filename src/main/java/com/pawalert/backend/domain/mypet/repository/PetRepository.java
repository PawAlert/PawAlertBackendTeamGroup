package com.pawalert.backend.domain.mypet.repository;


import com.pawalert.backend.domain.mypet.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<PetEntity, Long> {
}
