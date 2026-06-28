package com.payments.checkout.dto;

import com.payments.checkout.model.Metodo;
import com.payments.checkout.persistence.PagamentoEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Saída da API para o histórico de pagamentos (não expõe a entidade diretamente).
 */
public record PagamentoHistoricoResponse(

        Long id,
        Metodo metodo,
        BigDecimal valor,
        String moeda,
        String cliente,
        boolean sucesso,
        String idTransacao,
        String mensagem,
        LocalDateTime criadoEm
) {

    public static PagamentoHistoricoResponse de(PagamentoEntity e) {
        return new PagamentoHistoricoResponse(
                e.getId(),
                e.getMetodo(),
                e.getValor(),
                e.getMoeda(),
                e.getCliente(),
                e.isSucesso(),
                e.getIdTransacao(),
                e.getMensagem(),
                e.getCriadoEm());
    }
}
