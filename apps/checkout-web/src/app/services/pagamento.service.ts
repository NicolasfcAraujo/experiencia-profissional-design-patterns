import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import {
  Metodo,
  PagamentoHistorico,
  PagamentoRequest,
  PagamentoResponse
} from '../models/pagamento';

/**
 * Único ponto de acesso à API de pagamentos. O frontend nunca chama um endpoint
 * por provedor — sempre o mesmo POST /pagamentos, refletindo a Facade do backend.
 */
@Injectable({ providedIn: 'root' })
export class PagamentoService {
  private http = inject(HttpClient);
  private readonly base = 'http://localhost:8080/pagamentos';

  pagar(req: PagamentoRequest): Observable<PagamentoResponse> {
    return this.http.post<PagamentoResponse>(this.base, req);
  }

  historico(): Observable<PagamentoHistorico[]> {
    return this.http.get<PagamentoHistorico[]>(this.base);
  }

  metodos(): Observable<Metodo[]> {
    return this.http.get<Metodo[]>(`${this.base}/metodos`);
  }
}
