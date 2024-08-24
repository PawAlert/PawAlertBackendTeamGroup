package com.pawalert.backend.domain.user.model;

public record MyPageGetRequest(
        String uid,
        String email,
        String userName,
        String phoneNumber,
        String authProvider,
        String profileImageUrl,
        UserRole UserRoles
) {
}
