package com.payments.checkout.dto;

import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.Pagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Entrada da API. Validado com Bean Validation ({@code @Valid} no controller).
 * Não expõe nenhum tipo de SDK — só vocabulário de domínio.
 */
public record PagamentoRequest(

        @NotNull(message = "metodo é obrigatório")
        Metodo metodo,

        @NotNull(message = "valor é obrigatório")
        @Positive(message = "valor deve ser positivo")
        BigDecimal valor,

        @NotBlank(message = "moeda é obrigatória")
        String moeda,

        String descricao,

        @NotBlank(message = "cliente é obrigatório")
        String cliente
) {

    /** Converte o DTO de entrada no objeto de domínio consumido pela Facade. */
    public Pagamento paraDominio() {
        return Pagamento.builder()
                .metodo(metodo)
                .valor(valor)
                .moeda(moeda)
                .descricao(descricao)
                .cliente(cliente)
                .build();
    }
}
