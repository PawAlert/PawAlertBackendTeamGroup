package com.pawalert.backend;

import com.pawalert.backend.global.BaseEntity;
import com.pawalert.backend.global.Location;
import com.pawalert.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class FoundPetReport extends BaseEntity {
    @Id
    @Column(name = "found_pet_report_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "date_found", nullable = false)
    @Schema(description = "발견날짜")
    private LocalDate dateFound;

    @Schema(description = "발견 위도/경도")
    @Embedded
    private Location location;

    @Lob
    @Column(name = "description")
    @Schema(description = "특징 및 설명")
    private String description;

    @Size(max = 255)
    @Column(name = "photo_url")
    @Schema(description = "사진 URL")
    private String photoUrl;



}