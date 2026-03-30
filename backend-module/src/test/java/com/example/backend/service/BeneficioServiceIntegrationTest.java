package com.example.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.repository.BeneficioRepository;
import com.example.ejb.Beneficio;
import com.example.ejb.exception.BeneficioBusinessException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeneficioServiceIntegrationTest {

    @Autowired
    private BeneficioService beneficioService;

    @Autowired
    private BeneficioRepository beneficioRepository;

    private Beneficio origem;
    private Beneficio destino;

    @BeforeEach
    void setUp() {
        beneficioRepository.deleteAll();
        origem = beneficioRepository.save(new Beneficio(
            "Beneficio Origem",
            "Conta de origem",
            new BigDecimal("1000.00"),
            true
        ));
        destino = beneficioRepository.save(new Beneficio(
            "Beneficio Destino",
            "Conta de destino",
            new BigDecimal("300.00"),
            true
        ));
    }

    @Test
    void shouldTransferBalanceWithConsistency() {
        beneficioService.transfer(new TransferenciaRequest(origem.getId(), destino.getId(), new BigDecimal("250.00")));

        Beneficio origemAtualizada = beneficioRepository.findById(origem.getId()).orElseThrow();
        Beneficio destinoAtualizado = beneficioRepository.findById(destino.getId()).orElseThrow();

        assertThat(origemAtualizada.getValor()).isEqualByComparingTo("750.00");
        assertThat(destinoAtualizado.getValor()).isEqualByComparingTo("550.00");
    }

    @Test
    void shouldRejectTransferWhenBalanceIsInsufficient() {
        assertThatThrownBy(() ->
            beneficioService.transfer(new TransferenciaRequest(origem.getId(), destino.getId(), new BigDecimal("2000.00")))
        )
            .isInstanceOf(BeneficioBusinessException.class)
            .hasMessageContaining("Saldo insuficiente");

        Beneficio origemAtualizada = beneficioRepository.findById(origem.getId()).orElseThrow();
        Beneficio destinoAtualizado = beneficioRepository.findById(destino.getId()).orElseThrow();

        assertThat(origemAtualizada.getValor()).isEqualByComparingTo("1000.00");
        assertThat(destinoAtualizado.getValor()).isEqualByComparingTo("300.00");
    }

    @Test
    void shouldProvideCrudOperations() {
        BeneficioResponse criado = beneficioService.create(new BeneficioRequest(
            "Beneficio Novo",
            "Descricao",
            new BigDecimal("150.00"),
            true
        ));

        assertThat(criado.id()).isNotNull();

        BeneficioResponse atualizado = beneficioService.update(criado.id(), new BeneficioRequest(
            "Beneficio Atualizado",
            "Descricao atualizada",
            new BigDecimal("175.00"),
            true
        ));

        assertThat(atualizado.nome()).isEqualTo("Beneficio Atualizado");
        assertThat(beneficioService.listAll()).hasSize(3);

        beneficioService.delete(criado.id());
        assertThat(beneficioService.listAll()).hasSize(2);
    }
}
