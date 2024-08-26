package com.pawalert.backend.domain.hospital.repository;



import com.pawalert.backend.domain.hospital.entity.HospitalExcelInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HospitalExcelInfoRepository extends JpaRepository<HospitalExcelInfo, Long> {
    Optional<HospitalExcelInfo> findByLicenseNumber(String licenseNumber);
}
