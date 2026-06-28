package com.payments.checkout.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Value;

/**
 * Objeto de domínio que entra no contrato {@code GatewayPagamento.processar}.
 * Imutável (Lombok {@code @Value}). Independe de qualquer SDK.
 */
@Value
@Builder
public class Pagamento {

    Metodo metodo;
    BigDecimal valor;
    String moeda;
    String descricao;
    String cliente;
}
