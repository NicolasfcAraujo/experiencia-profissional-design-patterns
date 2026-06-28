package com.payments.checkout.model;

import lombok.Builder;
import lombok.Value;

/**
 * Resultado normalizado de um pagamento. Cada {@code *Adapter} traduz a resposta
 * (e os erros) do seu SDK para este formato comum — o resto do sistema só vê isto.
 */
@Value
@Builder
public class ResultadoPagamento {

    boolean sucesso;
    String idTransacao;
    String mensagem;
    Metodo metodo;

    public static ResultadoPagamento sucesso(Metodo metodo, String idTransacao, String mensagem) {
        return ResultadoPagamento.builder()
                .sucesso(true)
                .metodo(metodo)
                .idTransacao(idTransacao)
                .mensagem(mensagem)
                .build();
    }

    public static ResultadoPagamento falha(Metodo metodo, String mensagem) {
        return ResultadoPagamento.builder()
                .sucesso(false)
                .metodo(metodo)
                .mensagem(mensagem)
                .build();
    }
}
