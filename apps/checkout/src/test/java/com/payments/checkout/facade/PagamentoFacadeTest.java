package com.payments.checkout.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.payments.checkout.gateway.GatewayPagamento;
import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.Pagamento;
import com.payments.checkout.model.ResultadoPagamento;
import com.payments.checkout.persistence.PagamentoEntity;
import com.payments.checkout.persistence.PagamentoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PagamentoFacadeTest {

    @Mock
    GatewayPagamento stripeGateway;

    @Mock
    GatewayPagamento pixGateway;

    @Mock
    PagamentoRepository repository;

    private Pagamento pagamento(Metodo metodo) {
        return Pagamento.builder()
                .metodo(metodo)
                .valor(new BigDecimal("15.00"))
                .moeda("BRL")
                .cliente("ana@exemplo.com")
                .build();
    }

    @Test
    void selecionaAdapterCorretoPorMetodoEPersiste() {
        when(stripeGateway.metodoSuportado()).thenReturn(Metodo.STRIPE);
        when(pixGateway.metodoSuportado()).thenReturn(Metodo.PIX);

        ResultadoPagamento esperado = ResultadoPagamento.sucesso(Metodo.PIX, "PIX1", "ok");
        when(pixGateway.processar(any())).thenReturn(esperado);

        PagamentoFacade facade = new PagamentoFacade(List.of(stripeGateway, pixGateway), repository);
        Pagamento pagamento = pagamento(Metodo.PIX);

        ResultadoPagamento resultado = facade.pagar(pagamento);

        assertEquals(esperado, resultado);
        verify(pixGateway).processar(pagamento);
        verify(stripeGateway, never()).processar(any());
        verify(repository).save(any(PagamentoEntity.class));
    }

    @Test
    void lancaErroQuandoMetodoNaoSuportado() {
        when(stripeGateway.metodoSuportado()).thenReturn(Metodo.STRIPE);

        PagamentoFacade facade = new PagamentoFacade(List.of(stripeGateway), repository);

        assertThrows(IllegalArgumentException.class, () -> facade.pagar(pagamento(Metodo.PIX)));
        verify(repository, never()).save(any());
    }
}
