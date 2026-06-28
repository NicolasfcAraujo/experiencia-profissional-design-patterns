package com.payments.checkout.gateway.adapter;

import com.payments.checkout.gateway.GatewayPagamento;
import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.Pagamento;
import com.payments.checkout.model.ResultadoPagamento;
import com.payments.checkout.sdk.PixApiLegada;
import org.springframework.stereotype.Component;

/**
 * Padrão Adapter — adapta {@link PixApiLegada} (tudo String, retorno {@code String[]}
 * posicional) ao contrato {@link GatewayPagamento}. A estranheza da API legada para aqui.
 */
@Component
public class PixAdapter implements GatewayPagamento {

    private final PixApiLegada pixApiLegada;

    public PixAdapter(PixApiLegada pixApiLegada) {
        this.pixApiLegada = pixApiLegada;
    }

    @Override
    public Metodo metodoSuportado() {
        return Metodo.PIX;
    }

    @Override
    public ResultadoPagamento processar(Pagamento pagamento) {
        try {
            String[] resposta = pixApiLegada.executar("PAGAR", pagamento.getValor().toPlainString());

            boolean ok = resposta.length > 0 && "1".equals(resposta[0]);
            String txid = resposta.length > 1 ? resposta[1] : null;
            String mensagem = resposta.length > 2 ? resposta[2] : "";

            return ok
                    ? ResultadoPagamento.sucesso(Metodo.PIX, txid, "Pagamento Pix concluído")
                    : ResultadoPagamento.falha(Metodo.PIX, "Pagamento Pix falhou: " + mensagem);
        } catch (Exception e) {
            return ResultadoPagamento.falha(Metodo.PIX, "Erro no Pix: " + e.getMessage());
        }
    }
}
