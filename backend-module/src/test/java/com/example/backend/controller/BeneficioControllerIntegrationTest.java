package com.example.backend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.backend.repository.BeneficioRepository;
import com.example.ejb.Beneficio;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BeneficioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BeneficioRepository beneficioRepository;

    private Beneficio origem;
    private Beneficio destino;

    @BeforeEach
    void setUp() {
        beneficioRepository.deleteAll();
        origem = beneficioRepository.save(new Beneficio(
            "Beneficio A",
            "Descricao A",
            new BigDecimal("1000.00"),
            true
        ));
        destino = beneficioRepository.save(new Beneficio(
            "Beneficio B",
            "Descricao B",
            new BigDecimal("500.00"),
            true
        ));
    }

    @Test
    void shouldCreateAndListBeneficios() throws Exception {
        String payload = """
            {
              "nome": "Beneficio C",
              "descricao": "Descricao C",
              "valor": 320.00,
              "ativo": true
            }
            """;

        mockMvc.perform(post("/api/v1/beneficios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nome").value("Beneficio C"));

        mockMvc.perform(get("/api/v1/beneficios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void shouldTransferViaApiEndpoint() throws Exception {
        String payload = """
            {
              "fromId": %d,
              "toId": %d,
              "amount": 100.00
            }
            """.formatted(origem.getId(), destino.getId());

        mockMvc.perform(post("/api/v1/beneficios/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/beneficios/%d".formatted(origem.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.valor").value(900.00));

        mockMvc.perform(get("/api/v1/beneficios/%d".formatted(destino.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.valor").value(600.00));
    }

    @Test
    void shouldReturnBadRequestWhenTransferIsInvalid() throws Exception {
        String payload = """
            {
              "fromId": %d,
              "toId": %d,
              "amount": 5000.00
            }
            """.formatted(origem.getId(), destino.getId());

        mockMvc.perform(post("/api/v1/beneficios/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("BUSINESS_ERROR"));
    }
}
