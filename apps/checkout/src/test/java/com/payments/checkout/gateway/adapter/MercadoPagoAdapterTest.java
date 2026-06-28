package com.payments.checkout.gateway.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.Pagamento;
import com.payments.checkout.model.ResultadoPagamento;
import com.payments.checkout.sdk.MercadoPagoSDK;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MercadoPagoAdapterTest {

    @Mock
    MercadoPagoSDK mercadoPagoSDK;

    @InjectMocks
    MercadoPagoAdapter adapter;

    private Pagamento pagamento() {
        return Pagamento.builder()
                .metodo(Metodo.MERCADO_PAGO)
                .valor(new BigDecimal("25.50"))
                .moeda("BRL")
                .cliente("ana@exemplo.com")
                .build();
    }

    @Test
    void traduzPagamentoAprovado() {
        when(mercadoPagoSDK.criarPagamento(anyDouble(), anyString()))
                .thenReturn(new MercadoPagoSDK.MpResponse("approved", 987L, "ok"));

        ResultadoPagamento r = adapter.processar(pagamento());

        assertTrue(r.isSucesso());
        assertEquals("987", r.getIdTransacao());
        assertEquals(Metodo.MERCADO_PAGO, r.getMetodo());
    }

    @Test
    void traduzPagamentoRecusado() {
        when(mercadoPagoSDK.criarPagamento(anyDouble(), anyString()))
                .thenReturn(new MercadoPagoSDK.MpResponse("rejected", 0L, "saldo insuficiente"));

        ResultadoPagamento r = adapter.processar(pagamento());

        assertFalse(r.isSucesso());
        assertEquals(Metodo.MERCADO_PAGO, r.getMetodo());
    }

    @Test
    void normalizaErroDoSdk() {
        when(mercadoPagoSDK.criarPagamento(anyDouble(), anyString()))
                .thenThrow(new RuntimeException("indisponivel"));

        ResultadoPagamento r = adapter.processar(pagamento());

        assertFalse(r.isSucesso());
        assertTrue(r.getMensagem().contains("Erro no Mercado Pago"));
    }
}
