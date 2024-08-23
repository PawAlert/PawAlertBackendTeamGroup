package com.pawalert.backend;

import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.global.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class Pet extends BaseEntity {
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

    @Size(max = 255)
    @NotNull
    @Column(name = "pet_name", nullable = false)
    @Schema(description = "반려동물 이름")
    private String pet_name;

    @Size(max = 50)
    @Column(name = "species", length = 50)
    @Schema(description = "품종 : 강아지, 고양이")
    private String species;

    @Size(max = 50)
    @Column(name = "breed", length = 50)
    @Schema(description = "품종")
    private String breed;

    // todo 중성화 여부

    // todo 특징 (예: 경계심 있음)

    // todo 구조정보 ( 구조지역, 구조장소 등등 )





    @Size(max = 50)
    @Column(name = "color", length = 50)
    @Schema(description = "색상")
    private String color;

    @Column(name = "age")
    @Schema(description = "나이")
    private Integer age;

    @Lob
    @Column(name = "gender")
    @Schema(description = "성별")
    private String gender;

    @Size(max = 255)
    @Column(name = "microchip_id")
    @Schema(description = "마이크로칩 ID")
    private String microchipId;

    @Size(max = 255)
    @Column(name = "photo_url")
    @Schema(description = "사진 URL")
    private String photoUrl;


}