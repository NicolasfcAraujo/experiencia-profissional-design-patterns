package com.payments.checkout.sdk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * SDK simulado da Stripe. Fala "em centavos" ({@code long}) e devolve um objeto próprio
 * ({@link StripeCharge}) — formato que só o {@code StripeAdapter} deve conhecer.
 *
 * <p>Em produção viria da biblioteca oficial; aqui é simulado. A chave nunca é real:
 * vem de {@code STRIPE_API_KEY} (placeholder por padrão).</p>
 */
@Component
public class StripeClient {

    private final String apiKey;

    public StripeClient(@Value("${STRIPE_API_KEY:sk_test_placeholder}") String apiKey) {
        this.apiKey = apiKey;
    }

    public StripeCharge charge(long amountInCents, String currency) {
        if (amountInCents <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        boolean paid = apiKey != null && !apiKey.isBlank();
        String id = "ch_" + Long.toHexString(amountInCents * 31L + currency.hashCode());
        return new StripeCharge(id, paid, amountInCents, currency);
    }

    /** Resposta no formato da Stripe. */
    public static class StripeCharge {
        public final String id;
        public final boolean paid;
        public final long amount;
        public final String currency;

        public StripeCharge(String id, boolean paid, long amount, String currency) {
            this.id = id;
            this.paid = paid;
            this.amount = amount;
            this.currency = currency;
        }
    }
}
