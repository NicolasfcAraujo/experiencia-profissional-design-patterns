package com.payments.checkout.sdk;

import org.springframework.stereotype.Component;

/**
 * SDK simulado do Mercado Pago. Trabalha com {@code double}, moeda e devolve um
 * {@link MpResponse} cujo status é uma String ("approved"/"rejected") e o id é {@code Long}
 * — formato diferente da Stripe, isolado no {@code MercadoPagoAdapter}.
 */
@Component
public class MercadoPagoSDK {

    public MpResponse criarPagamento(double valor, String moeda) {
        if (valor <= 0) {
            return new MpResponse("rejected", 0L, "valor invalido");
        }
        long paymentId = Math.abs((long) (valor * 100) * 7L + moeda.hashCode());
        return new MpResponse("approved", paymentId, "ok");
    }

    /** Resposta no formato do Mercado Pago. */
    public static class MpResponse {
        public final String status;
        public final Long paymentId;
        public final String detail;

        public MpResponse(String status, Long paymentId, String detail) {
            this.status = status;
            this.paymentId = paymentId;
            this.detail = detail;
        }
    }
}
