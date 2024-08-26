package com.pawalert.backend.domain.hospital.repository;

import com.pawalert.backend.domain.hospital.entity.HospitalDoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalDoctorRepository extends JpaRepository<HospitalDoctorEntity, Long> {
    //userId 로 검색
    Optional<HospitalDoctorEntity> findByUserId(Long userId);
}
