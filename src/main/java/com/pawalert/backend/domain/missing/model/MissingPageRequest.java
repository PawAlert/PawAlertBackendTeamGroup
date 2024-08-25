package com.pawalert.backend.domain.missing.model;

public record MissingPageRequest(
        String title,
        String Content,
        String imageUrl,
        String address
) {
}
