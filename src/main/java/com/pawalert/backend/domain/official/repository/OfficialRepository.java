package com.pawalert.backend.domain.official.repository;

import com.pawalert.backend.domain.official.entity.OfficialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfficialRepository extends JpaRepository<OfficialEntity, Long> {
    Optional<OfficialEntity> findByUserUid(String userUid);
}
