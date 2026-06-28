package com.payments.checkout.controller;

import com.payments.checkout.dto.PagamentoHistoricoResponse;
import com.payments.checkout.dto.PagamentoRequest;
import com.payments.checkout.dto.PagamentoResponse;
import com.payments.checkout.facade.PagamentoFacade;
import com.payments.checkout.model.Metodo;
import com.payments.checkout.model.ResultadoPagamento;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Cliente da Facade. Sem regra de negócio: recebe a requisição, valida, delega à
 * {@link PagamentoFacade} e monta a resposta. Não conhece adapters nem SDKs.
 */
@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoFacade facade;

    public PagamentoController(PagamentoFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public PagamentoResponse pagar(@Valid @RequestBody PagamentoRequest request) {
        ResultadoPagamento resultado = facade.pagar(request.paraDominio());
        return PagamentoResponse.de(resultado);
    }

    @GetMapping
    public List<PagamentoHistoricoResponse> historico() {
        return facade.historico().stream().map(PagamentoHistoricoResponse::de).toList();
    }

    @GetMapping("/metodos")
    public List<Metodo> metodos() {
        return facade.metodosSuportados();
    }
}
