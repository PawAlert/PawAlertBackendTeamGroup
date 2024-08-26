package com.pawalert.backend.domain.hospital.repository;
import com.pawalert.backend.domain.hospital.entity.HospitalExcelInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HospitalExcelInfoRepository extends JpaRepository<HospitalExcelInfoEntity, Long> {
    Optional<HospitalExcelInfoEntity> findByLicenseNumber(String licenseNumber);
}
