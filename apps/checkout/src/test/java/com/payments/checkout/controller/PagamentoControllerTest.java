package com.payments.checkout.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.payments.checkout.facade.PagamentoFacade;
import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.ResultadoPagamento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PagamentoController.class)
class PagamentoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PagamentoFacade facade;

    @Test
    void retorna200NoCaminhoFeliz() throws Exception {
        when(facade.pagar(any()))
                .thenReturn(ResultadoPagamento.sucesso(Metodo.PIX, "PIX1", "concluido"));

        String json = """
                {"metodo":"PIX","valor":10.00,"moeda":"BRL","cliente":"ana@exemplo.com"}
                """;

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucesso").value(true))
                .andExpect(jsonPath("$.idTransacao").value("PIX1"));
    }

    @Test
    void retorna400ComPayloadInvalido() throws Exception {
        String json = """
                {"metodo":"PIX","valor":-5,"moeda":"","cliente":""}
                """;

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("validacao"));
    }
}
