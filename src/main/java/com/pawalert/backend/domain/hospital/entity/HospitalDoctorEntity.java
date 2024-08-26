package com.pawalert.backend.domain.hospital.entity;

import com.pawalert.backend.domain.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalDoctorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "병원 전화번호")
    @Column(name = "doctor_phone_number")
    private String phoneNumber;

    @Schema(description = "병원 이름")
    @Column(name = "doctor_hospital_name")
    private String hospitalName;

    @Schema(description = "병원 주소")
    @Column(name = "doctor_hospital_address")
    private String address;

    @Schema(description = "의사 인허가번호")
    @Column(name = "doctor_license_number")
    private String licenseNumber;

    @Schema(description = "의사 전공 예) 대형 동물 진료, 특수 동물 진료")
    @Column(name = "doctor_major")
    private String major;

    @Schema(description = "사용자 ID")
    @Column(name = "user_id")
    private Long userId;
}
