package com.pawalert.backend.domain.user.entity;

import com.pawalert.backend.domain.mypet.entity.PetEntity;
import com.pawalert.backend.global.BaseEntity;
import com.pawalert.backend.domain.user.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @Column(name = "user_name")
    private String userName;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Size(max = 255)
    @Column(name = "password")
    private String password;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "uid")
    @Schema(description = "소셜로그인시 제공되는 고유 아이디")
    private String uid;

    @Size(max = 50)
    @Column(name = "auth_provider", length = 50)
    @Schema(description = "소셜정보, google, kakao, naver, localuser")
    private String authProvider;

    @Size(max = 255)
    @Column(name = "profile_picture_url")
    @Schema(description = "프로필 사진 URL")
    private String profilePictureUrl;

    @Enumerated(EnumType.STRING) // enum 값을 문자열로 저장
    @Column(name = "role")
    @Schema(description = "USER, ADMIN, ASSOCIATION_USER")
    private UserRole role = UserRole.ROLE_USER;


    // 유저 - 반려동물 1:Nㅜ
    @OneToMany(mappedBy = "user",orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PetEntity> pets;



}