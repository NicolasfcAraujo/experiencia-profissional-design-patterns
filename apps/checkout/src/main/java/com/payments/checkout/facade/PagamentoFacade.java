package com.payments.checkout.facade;

import com.payments.checkout.gateway.GatewayPagamento;
import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.Pagamento;
import com.payments.checkout.model.ResultadoPagamento;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Padrão Facade — porta única do subsistema de pagamentos. O controller fala só com ela.
 *
 * <p>Recebe todos os {@link GatewayPagamento} por injeção e monta um registro
 * {@code Map<Metodo, GatewayPagamento>} a partir de {@code metodoSuportado()} — então a
 * seleção é uma busca no mapa, sem nenhum {@code if/else} ou {@code switch} de provedor.</p>
 */
@Service
public class PagamentoFacade {

    private final Map<Metodo, GatewayPagamento> gateways;

    public PagamentoFacade(List<GatewayPagamento> adapters) {
        this.gateways = adapters.stream()
                .collect(Collectors.toMap(GatewayPagamento::metodoSuportado, Function.identity()));
    }

    public ResultadoPagamento pagar(Pagamento pagamento) {
        GatewayPagamento gateway = gateways.get(pagamento.getMetodo());
        if (gateway == null) {
            throw new IllegalArgumentException("Método de pagamento não suportado: " + pagamento.getMetodo());
        }
        return gateway.processar(pagamento);
    }
}
