package com.payments.checkout.model;

/**
 * Métodos de pagamento suportados. Serve de chave para a {@code PagamentoFacade}
 * selecionar o {@code *Adapter} correspondente — sem nenhum {@code if/else} de provedor.
 */
public enum Metodo {
    STRIPE,
    MERCADO_PAGO,
    PIX
}
