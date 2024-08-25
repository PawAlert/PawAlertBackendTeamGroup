package com.pawalert.backend.domain.missing.model;

import java.time.LocalDateTime;

public record MissingSearchCriteria(
        String title,
        String description,
        String location,
        LocalDateTime dateLostStart,
        LocalDateTime dateLostEnd,
        MissingStatus status
) {
}
