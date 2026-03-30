package com.example.backend.service;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.repository.BeneficioRepository;
import com.example.ejb.Beneficio;
import com.example.ejb.BeneficioEjbService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BeneficioService {

    private final BeneficioRepository beneficioRepository;
    private final BeneficioEjbService beneficioEjbService;

    public BeneficioService(BeneficioRepository beneficioRepository, BeneficioEjbService beneficioEjbService) {
        this.beneficioRepository = beneficioRepository;
        this.beneficioEjbService = beneficioEjbService;
    }

    public List<BeneficioResponse> listAll() {
        return beneficioRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public BeneficioResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public BeneficioResponse create(BeneficioRequest request) {
        Beneficio entity = new Beneficio(
            request.nome(),
            request.descricao(),
            request.valor(),
            request.ativo()
        );
        return toResponse(beneficioRepository.save(entity));
    }

    @Transactional
    public BeneficioResponse update(Long id, BeneficioRequest request) {
        Beneficio existing = getOrThrow(id);
        existing.setNome(request.nome());
        existing.setDescricao(request.descricao());
        existing.setValor(request.valor());
        existing.setAtivo(request.ativo());
        return toResponse(beneficioRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        Beneficio existing = getOrThrow(id);
        beneficioRepository.delete(existing);
    }

    @Transactional
    public void transfer(TransferenciaRequest request) {
        beneficioEjbService.transfer(request.fromId(), request.toId(), request.amount());
    }

    private Beneficio getOrThrow(Long id) {
        return beneficioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Beneficio id " + id + " nao encontrado."));
    }

    private BeneficioResponse toResponse(Beneficio beneficio) {
        return new BeneficioResponse(
            beneficio.getId(),
            beneficio.getNome(),
            beneficio.getDescricao(),
            beneficio.getValor(),
            beneficio.getAtivo(),
            beneficio.getVersion()
        );
    }
}
