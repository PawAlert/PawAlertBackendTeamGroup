package com.pawalert.backend.domain.hospital.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class HospitalExcelInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hospital_name")
    private String name;
    @Column(name = "hospital_phone_number")
    private String phoneNumber;
    @Column(name = "hospital_address")
    private String address;
    @Column(name = "hospital_license_number")
    private String licenseNumber;

}
