package com.pawalert.backend.domain.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "hospital_excel_info")
public class HospitalExcelInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "병원 이름")
    @Column(name = "hospital_name")
    private String name;

    @Schema(description = "병원 전화번호")
    @Column(name = "hospital_phone_number")
    private String phoneNumber;

    @Schema(name = "병원 주소")
    @Column(name = "hospital_address")
    private String address;

    @Schema(name = "의사 인허가번호")
    @Column(name = "hospital_license_number")
    private String licenseNumber;

}
