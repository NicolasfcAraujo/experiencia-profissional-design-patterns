package com.payments.checkout.gateway.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.Pagamento;
import com.payments.checkout.model.ResultadoPagamento;
import com.payments.checkout.sdk.PixApiLegada;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PixAdapterTest {

    @Mock
    PixApiLegada pixApiLegada;

    @InjectMocks
    PixAdapter adapter;

    private Pagamento pagamento() {
        return Pagamento.builder()
                .metodo(Metodo.PIX)
                .valor(new BigDecimal("42.00"))
                .moeda("BRL")
                .cliente("ana@exemplo.com")
                .build();
    }

    @Test
    void traduzRetornoPosicionalDeSucesso() {
        when(pixApiLegada.executar(anyString(), anyString()))
                .thenReturn(new String[] {"1", "PIXABC", "concluido"});

        ResultadoPagamento r = adapter.processar(pagamento());

        assertTrue(r.isSucesso());
        assertEquals("PIXABC", r.getIdTransacao());
        assertEquals(Metodo.PIX, r.getMetodo());
    }

    @Test
    void traduzRetornoPosicionalDeFalha() {
        when(pixApiLegada.executar(anyString(), anyString()))
                .thenReturn(new String[] {"0", "", "valor invalido"});

        ResultadoPagamento r = adapter.processar(pagamento());

        assertFalse(r.isSucesso());
        assertEquals(Metodo.PIX, r.getMetodo());
    }

    @Test
    void normalizaErroDoSdk() {
        when(pixApiLegada.executar(anyString(), anyString()))
                .thenThrow(new RuntimeException("conexao recusada"));

        ResultadoPagamento r = adapter.processar(pagamento());

        assertFalse(r.isSucesso());
        assertTrue(r.getMensagem().contains("Erro no Pix"));
    }
}
