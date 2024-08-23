package com.pawalert.backend.domain.organization.entity;

import com.pawalert.backend.global.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AnimalRescueOrganization extends BaseEntity {
    @Id
    @Column(name = "organization_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    @Schema(description = "동물 구조 단체 이름")
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "contact_email", nullable = false)
    @Schema(description = "연락 이메일")
    private String contactEmail;

    @Size(max = 20)
    @Column(name = "contact_phone", length = 20)
    @Schema(description = "연락 전화번호")
    private String contactPhone;

    @Size(max = 255)
    @Column(name = "address")
    @Schema(description = "주소")
    private String address;

    @Size(max = 255)
    @Column(name = "website_url")
    @Schema(description = "웹사이트 URL")
    private String websiteUrl;



}