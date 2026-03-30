package com.example.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransferenciaRequest(
    @NotNull(message = "fromId e obrigatorio")
    Long fromId,

    @NotNull(message = "toId e obrigatorio")
    Long toId,

    @NotNull(message = "amount e obrigatorio")
    @DecimalMin(value = "0.01", message = "amount deve ser maior que zero")
    BigDecimal amount
) {
}
