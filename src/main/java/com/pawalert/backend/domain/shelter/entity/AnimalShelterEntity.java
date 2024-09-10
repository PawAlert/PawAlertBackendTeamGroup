package com.pawalert.backend.domain.shelter.entity;

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

    private String jurisdiction; // 관할구역
    private String shelterName; // 보호센터명
    private String phoneNumber; // 전화번호
    private String address; // 주소

}