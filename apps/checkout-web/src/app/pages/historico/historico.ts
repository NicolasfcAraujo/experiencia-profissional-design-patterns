import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { METODO_LABEL, PagamentoHistorico } from '../../models/pagamento';
import { PagamentoService } from '../../services/pagamento.service';

@Component({
  selector: 'app-historico',
  imports: [DatePipe, DecimalPipe],
  templateUrl: './historico.html'
})
export class Historico implements OnInit {
  private service = inject(PagamentoService);

  readonly label = METODO_LABEL;

  itens = signal<PagamentoHistorico[]>([]);
  carregando = signal(false);
  erro = signal<string | null>(null);

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando.set(true);
    this.erro.set(null);
    this.service.historico().subscribe({
      next: (i) => {
        this.itens.set(i);
        this.carregando.set(false);
      },
      error: () => {
        this.erro.set('Falha ao carregar o histórico. A API está rodando?');
        this.carregando.set(false);
      }
    });
  }
}
