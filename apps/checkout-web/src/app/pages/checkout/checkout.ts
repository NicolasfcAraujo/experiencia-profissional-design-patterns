import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import {
  Metodo,
  METODO_LABEL,
  PagamentoRequest,
  PagamentoResponse
} from '../../models/pagamento';
import { PagamentoService } from '../../services/pagamento.service';

@Component({
  selector: 'app-checkout',
  imports: [FormsModule],
  templateUrl: './checkout.html'
})
export class Checkout implements OnInit {
  private service = inject(PagamentoService);

  readonly label = METODO_LABEL;

  // Logo de cada provedor (servidos de public/logos).
  readonly logo: Record<Metodo, string> = {
    STRIPE: 'logos/stripe.png',
    MERCADO_PAGO: 'logos/mercadopago.png',
    PIX: 'logos/pix.png'
  };

  // Realce na cor característica da marca quando selecionado (classes literais p/ o Tailwind).
  readonly estilo: Record<Metodo, string> = {
    STRIPE: 'border-2 border-[#635BFF] bg-[#635BFF]/10',
    MERCADO_PAGO: 'border-2 border-[#009EE3] bg-[#009EE3]/10',
    PIX: 'border-2 border-[#32BCAD] bg-[#32BCAD]/10'
  };
  readonly estiloInativo = 'border border-slate-300 hover:bg-slate-50';

  metodos = signal<Metodo[]>([]);
  resultado = signal<PagamentoResponse | null>(null);
  comparativo = signal<PagamentoResponse[] | null>(null);
  carregando = signal(false);
  erro = signal<string | null>(null);

  // modelo do formulário
  valor = 149.9;
  moeda = 'BRL';
  cliente = 'ana@exemplo.com';
  descricao = '';
  metodo: Metodo = 'STRIPE';

  ngOnInit(): void {
    this.service.metodos().subscribe({
      next: (m) => {
        this.metodos.set(m);
        if (m.length && !m.includes(this.metodo)) {
          this.metodo = m[0];
        }
      },
      error: () => this.erro.set('Não foi possível carregar os métodos. A API está rodando?')
    });
  }

  private montarRequest(metodo: Metodo): PagamentoRequest {
    return {
      metodo,
      valor: this.valor,
      moeda: this.moeda,
      cliente: this.cliente,
      descricao: this.descricao || undefined
    };
  }

  pagar(): void {
    this.erro.set(null);
    this.comparativo.set(null);
    this.carregando.set(true);
    this.service.pagar(this.montarRequest(this.metodo)).subscribe({
      next: (r) => {
        this.resultado.set(r);
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Falha ao chamar a API.');
        this.carregando.set(false);
      }
    });
  }

  rodarOsTres(): void {
    this.erro.set(null);
    this.resultado.set(null);
    this.carregando.set(true);
    const requisicoes = this.metodos().map((m) => this.service.pagar(this.montarRequest(m)));
    forkJoin(requisicoes).subscribe({
      next: (respostas) => {
        this.comparativo.set(respostas);
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Falha ao chamar a API.');
        this.carregando.set(false);
      }
    });
  }
}
