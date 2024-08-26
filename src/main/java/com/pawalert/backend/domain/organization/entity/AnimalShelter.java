package com.pawalert.backend.domain.organization.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "animal_shelters")
public class AnimalShelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 번호 (Primary Key)

    private String jurisdiction; // 관할구역
    private String shelterName; // 보호센터명
    private String phoneNumber; // 전화번호
    private String address; // 주소
}