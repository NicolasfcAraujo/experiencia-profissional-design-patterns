package com.payments.checkout.gateway.adapter;

import com.payments.checkout.gateway.GatewayPagamento;
import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.Pagamento;
import com.payments.checkout.model.ResultadoPagamento;
import com.payments.checkout.sdk.MercadoPagoSDK;
import org.springframework.stereotype.Component;

/**
 * Padrão Adapter — adapta {@link MercadoPagoSDK} (double, status em String, id Long)
 * ao contrato {@link GatewayPagamento}. As peculiaridades do Mercado Pago ficam aqui.
 */
@Component
public class MercadoPagoAdapter implements GatewayPagamento {

    private final MercadoPagoSDK mercadoPagoSDK;

    public MercadoPagoAdapter(MercadoPagoSDK mercadoPagoSDK) {
        this.mercadoPagoSDK = mercadoPagoSDK;
    }

    @Override
    public Metodo metodoSuportado() {
        return Metodo.MERCADO_PAGO;
    }

    @Override
    public ResultadoPagamento processar(Pagamento pagamento) {
        try {
            String moeda = pagamento.getMoeda() == null ? "BRL" : pagamento.getMoeda().toUpperCase();

            MercadoPagoSDK.MpResponse resposta =
                    mercadoPagoSDK.criarPagamento(pagamento.getValor().doubleValue(), moeda);

            return "approved".equalsIgnoreCase(resposta.status)
                    ? ResultadoPagamento.sucesso(Metodo.MERCADO_PAGO, String.valueOf(resposta.paymentId),
                            "Pagamento aprovado pelo Mercado Pago")
                    : ResultadoPagamento.falha(Metodo.MERCADO_PAGO,
                            "Pagamento recusado pelo Mercado Pago: " + resposta.detail);
        } catch (Exception e) {
            return ResultadoPagamento.falha(Metodo.MERCADO_PAGO, "Erro no Mercado Pago: " + e.getMessage());
        }
    }
}
