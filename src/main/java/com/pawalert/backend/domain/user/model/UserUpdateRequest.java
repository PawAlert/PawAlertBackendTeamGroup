package com.pawalert.backend.domain.user.model;

public record UserUpdateRequest(
        String username,
        String phoneNumber,
        String password
) {
}
