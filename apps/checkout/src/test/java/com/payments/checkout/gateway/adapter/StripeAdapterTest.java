package com.payments.checkout.gateway.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.Pagamento;
import com.payments.checkout.model.ResultadoPagamento;
import com.payments.checkout.sdk.StripeClient;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StripeAdapterTest {

    @Mock
    StripeClient stripeClient;

    @InjectMocks
    StripeAdapter adapter;

    private Pagamento pagamento() {
        return Pagamento.builder()
                .metodo(Metodo.STRIPE)
                .valor(new BigDecimal("10.00"))
                .moeda("BRL")
                .cliente("ana@exemplo.com")
                .build();
    }

    @Test
    void traduzCobrancaAprovada() {
        when(stripeClient.charge(anyLong(), anyString()))
                .thenReturn(new StripeClient.StripeCharge("ch_123", true, 1000, "brl"));

        ResultadoPagamento r = adapter.processar(pagamento());

        assertTrue(r.isSucesso());
        assertEquals("ch_123", r.getIdTransacao());
        assertEquals(Metodo.STRIPE, r.getMetodo());
    }

    @Test
    void traduzCobrancaRecusada() {
        when(stripeClient.charge(anyLong(), anyString()))
                .thenReturn(new StripeClient.StripeCharge("ch_456", false, 1000, "brl"));

        ResultadoPagamento r = adapter.processar(pagamento());

        assertFalse(r.isSucesso());
        assertEquals(Metodo.STRIPE, r.getMetodo());
    }

    @Test
    void normalizaErroDoSdk() {
        when(stripeClient.charge(anyLong(), anyString()))
                .thenThrow(new RuntimeException("timeout"));

        ResultadoPagamento r = adapter.processar(pagamento());

        assertFalse(r.isSucesso());
        assertTrue(r.getMensagem().contains("Erro na Stripe"));
    }
}
