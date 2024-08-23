package com.pawalert.backend.domain.user.repository;

import com.pawalert.backend.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUid(String uid);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
