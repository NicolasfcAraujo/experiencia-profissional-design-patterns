package com.payments.checkout.sdk;

import org.springframework.stereotype.Component;

/**
 * API "legada" de Pix simulada. Contrato propositalmente esquisito: recebe tudo como
 * String e devolve {@code String[]} posicional {@code [status, txid, mensagem]}, onde
 * status é "1" (ok) ou "0" (falha). Toda essa estranheza fica confinada no {@code PixAdapter}.
 */
@Component
public class PixApiLegada {

    public String[] executar(String operacao, String valor) {
        try {
            double v = Double.parseDouble(valor);
            if (!"PAGAR".equals(operacao) || v <= 0) {
                return new String[] {"0", "", "operacao ou valor invalido"};
            }
            String txid = "PIX" + Long.toHexString((long) (v * 1000)).toUpperCase();
            return new String[] {"1", txid, "concluido"};
        } catch (NumberFormatException e) {
            return new String[] {"0", "", "valor nao numerico"};
        }
    }
}
