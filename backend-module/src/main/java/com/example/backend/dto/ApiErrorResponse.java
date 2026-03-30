package com.example.backend.dto;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(
    String error,
    String message,
    Instant timestamp,
    Map<String, String> details
) {
}
