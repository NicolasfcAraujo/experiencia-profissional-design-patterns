package com.payments.checkout.gateway.adapter;

import com.payments.checkout.gateway.GatewayPagamento;
import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.Pagamento;
import com.payments.checkout.model.ResultadoPagamento;
import com.payments.checkout.sdk.StripeClient;
import org.springframework.stereotype.Component;

/**
 * Padrão Adapter — adapta {@link StripeClient} (centavos, objeto próprio) ao contrato
 * {@link GatewayPagamento}. Toda a tradução e o tratamento de erro da Stripe ficam aqui.
 */
@Component
public class StripeAdapter implements GatewayPagamento {

    private final StripeClient stripeClient;

    public StripeAdapter(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    @Override
    public Metodo metodoSuportado() {
        return Metodo.STRIPE;
    }

    @Override
    public ResultadoPagamento processar(Pagamento pagamento) {
        try {
            long centavos = pagamento.getValor().movePointRight(2).longValueExact();
            String moeda = pagamento.getMoeda() == null ? "brl" : pagamento.getMoeda().toLowerCase();

            StripeClient.StripeCharge charge = stripeClient.charge(centavos, moeda);

            return charge.paid
                    ? ResultadoPagamento.sucesso(Metodo.STRIPE, charge.id, "Pagamento aprovado pela Stripe")
                    : ResultadoPagamento.falha(Metodo.STRIPE, "Pagamento recusado pela Stripe");
        } catch (Exception e) {
            return ResultadoPagamento.falha(Metodo.STRIPE, "Erro na Stripe: " + e.getMessage());
        }
    }
}
