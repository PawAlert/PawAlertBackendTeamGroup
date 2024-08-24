package com.pawalert.backend.domain.missing.model;

public record ChangeMissingStatusRecord(
        Long missingReportId,
        MissingStatus status
) {
}
