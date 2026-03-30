package com.example.backend.controller;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.service.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/beneficios")
@Tag(name = "Beneficios")
public class BeneficioController {

    private final BeneficioService beneficioService;

    public BeneficioController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @GetMapping
    @Operation(summary = "Lista todos os beneficios")
    public List<BeneficioResponse> list() {
        return beneficioService.listAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um beneficio por id")
    public BeneficioResponse findById(@PathVariable Long id) {
        return beneficioService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria um novo beneficio")
    public BeneficioResponse create(@Valid @RequestBody BeneficioRequest request) {
        return beneficioService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um beneficio existente")
    public BeneficioResponse update(@PathVariable Long id, @Valid @RequestBody BeneficioRequest request) {
        return beneficioService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove um beneficio")
    public void delete(@PathVariable Long id) {
        beneficioService.delete(id);
    }

    @PostMapping("/transferencias")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Transfere saldo entre beneficios")
    public void transfer(@Valid @RequestBody TransferenciaRequest request) {
        beneficioService.transfer(request);
    }
}
