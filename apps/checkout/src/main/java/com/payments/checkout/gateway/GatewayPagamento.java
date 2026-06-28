package com.payments.checkout.gateway;

import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.Pagamento;
import com.payments.checkout.model.ResultadoPagamento;

/**
 * Padrão Adapter — <b>Target</b>: contrato comum do domínio que a Facade enxerga.
 * Cada {@code *Adapter} implementa esta interface traduzindo um SDK específico.
 */
public interface GatewayPagamento {

    /** Contrato único de processamento de pagamento. */
    ResultadoPagamento processar(Pagamento pagamento);

    /**
     * Método que este adapter atende. Permite à {@code PagamentoFacade} montar o
     * registro {@code Map<Metodo, GatewayPagamento>} sem conhecer provedores concretos.
     */
    Metodo metodoSuportado();
}
