package com.pawalert.backend.domain.organization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "animal_shelters")
public class AnimalShelterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 번호 (Primary Key)

    @Column(name = "animal_shelters_jurisdiction")
    private String jurisdiction; // 관할구역
    @Column(name = "animal_shelters_name")
    private String shelterName; // 보호센터명
    @Column(name = "animal_shelters_phone_number")
    private String phoneNumber; // 전화번호
    @Column(name = "animal_shelters_address")
    private String address; // 주소
}