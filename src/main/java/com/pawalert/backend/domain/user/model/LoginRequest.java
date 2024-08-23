package com.pawalert.backend.domain.user.model;

public record LoginRequest(
         String email,
         String password
) {
}
