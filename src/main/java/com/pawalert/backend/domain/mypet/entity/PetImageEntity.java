package com.pawalert.backend.domain.mypet.entity;

import com.pawalert.backend.global.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pet_images")
public class PetImageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @Size(max = 255)
    @Column(name = "photo_url")
    @Schema(description = "사진 URL")
    private String photoUrl;


    @Column(name = "is_deleted")
    @Schema(description = "이미지 삭제 여부")
    private boolean isDeleted;
}
