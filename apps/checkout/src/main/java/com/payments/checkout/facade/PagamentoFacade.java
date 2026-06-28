package com.payments.checkout.facade;

import com.payments.checkout.gateway.GatewayPagamento;
import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.Pagamento;
import com.payments.checkout.model.ResultadoPagamento;
import com.payments.checkout.persistence.PagamentoEntity;
import com.payments.checkout.persistence.PagamentoRepository;
import java.time.LocalDateTime;
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
 *
 * <p>Também persiste o histórico de cada pagamento (H2). A persistência é detalhe da
 * Facade: controller e adapters não conhecem o banco.</p>
 */
@Service
public class PagamentoFacade {

    private final Map<Metodo, GatewayPagamento> gateways;
    private final PagamentoRepository repository;

    public PagamentoFacade(List<GatewayPagamento> adapters, PagamentoRepository repository) {
        this.gateways = adapters.stream()
                .collect(Collectors.toMap(GatewayPagamento::metodoSuportado, Function.identity()));
        this.repository = repository;
    }

    public ResultadoPagamento pagar(Pagamento pagamento) {
        GatewayPagamento gateway = gateways.get(pagamento.getMetodo());
        if (gateway == null) {
            throw new IllegalArgumentException("Método de pagamento não suportado: " + pagamento.getMetodo());
        }

        ResultadoPagamento resultado = gateway.processar(pagamento);
        repository.save(paraEntidade(pagamento, resultado));
        return resultado;
    }

    private PagamentoEntity paraEntidade(Pagamento pagamento, ResultadoPagamento resultado) {
        return PagamentoEntity.builder()
                .metodo(pagamento.getMetodo())
                .valor(pagamento.getValor())
                .moeda(pagamento.getMoeda())
                .cliente(pagamento.getCliente())
                .sucesso(resultado.isSucesso())
                .idTransacao(resultado.getIdTransacao())
                .mensagem(resultado.getMensagem())
                .criadoEm(LocalDateTime.now())
                .build();
    }
}
