package com.payments.checkout.dto;

import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.ResultadoPagamento;

/**
 * Saída da API. Espelha o {@link ResultadoPagamento} de domínio sem vazar SDK.
 */
public record PagamentoResponse(

        boolean sucesso,
        String idTransacao,
        String mensagem,
        Metodo metodo
) {

    public static PagamentoResponse de(ResultadoPagamento resultado) {
        return new PagamentoResponse(
                resultado.isSucesso(),
                resultado.getIdTransacao(),
                resultado.getMensagem(),
                resultado.getMetodo());
    }
}
