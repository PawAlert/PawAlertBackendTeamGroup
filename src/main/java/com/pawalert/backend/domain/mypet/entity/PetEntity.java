package com.pawalert.backend.domain.mypet.entity;

import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.global.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetEntity extends BaseEntity {
    @Id
    @Column(name = "pet_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "사용자 ID")
    private UserEntity user;

    @Size(max = 50)
    @NotNull
    @Column(name = "pet_name", nullable = false)
    @Schema(description = "반려동물 이름")
    private String petName;

    @Size(max = 50)
    @Column(name = "species", length = 50)
    @Schema(description = "품종 : 강아지, 고양이")
    private String species;

    @Size(max = 50)
    @Column(name = "breed", length = 50)
    @Schema(description = "상세품종")
    private String breed;

    // todo 중성화 여부
    @Column(name = "neutering")
    @Schema(description = "중성화 여부")
    private boolean neutering;

    @Size(max = 50)
    @Column(name = "color", length = 50)
    @Schema(description = "색상")
    private String color;

    @Column(name = "age")
    @Schema(description = "나이")
    private int age;

    @Lob
    @Column(name = "gender")
    @Schema(description = "성별")
    private String gender;

    @Size(max = 255)
    @Column(name = "microchip_id")
    @Schema(description = "마이크로칩 ID")
    private String microchipId;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "반려동물 사진 목록")
    private List<PetImageEntity> PetImages;

    @Schema(description = "특징 : 우리집 강아지는 기여워요")
    @Column(name = "description")
    private String description;

    @Column(name = "deleted_pet")
    @Schema(description = "삭제 여부")
    private boolean deleted;

}