package com.payments.checkout.persistence;

import com.payments.checkout.model.Metodo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Registro persistido de cada tentativa de pagamento (histórico no H2).
 * Camada de persistência — fica fora do padrão; quem a usa é a {@code PagamentoFacade}.
 */
@Entity
@Table(name = "pagamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Metodo metodo;

    private BigDecimal valor;
    private String moeda;
    private String cliente;

    private boolean sucesso;

    @Column(name = "id_transacao")
    private String idTransacao;

    private String mensagem;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
}
