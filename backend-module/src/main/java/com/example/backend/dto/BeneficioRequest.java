package com.example.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record BeneficioRequest(
    @NotBlank(message = "nome e obrigatorio")
    @Size(max = 100, message = "nome deve ter no maximo 100 caracteres")
    String nome,

    @Size(max = 255, message = "descricao deve ter no maximo 255 caracteres")
    String descricao,

    @NotNull(message = "valor e obrigatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "valor nao pode ser negativo")
    BigDecimal valor,

    @NotNull(message = "ativo e obrigatorio")
    Boolean ativo
) {
}
