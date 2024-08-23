package com.pawalert.backend.domain.user.model;

public record RegisterRequest(
        String email,
        String username,
        String password
) {
}
