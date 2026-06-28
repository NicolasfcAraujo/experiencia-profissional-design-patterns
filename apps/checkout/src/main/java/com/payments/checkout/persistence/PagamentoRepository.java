package com.payments.checkout.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring Data JPA do histórico de pagamentos (tabela {@code pagamento} no H2).
 */
public interface PagamentoRepository extends JpaRepository<PagamentoEntity, Long> {
}
