export type Metodo = 'STRIPE' | 'MERCADO_PAGO' | 'PIX';

export interface PagamentoRequest {
  metodo: Metodo;
  valor: number;
  moeda: string;
  descricao?: string;
  cliente: string;
}

export interface PagamentoResponse {
  sucesso: boolean;
  idTransacao: string | null;
  mensagem: string;
  metodo: Metodo;
}

export interface PagamentoHistorico {
  id: number;
  metodo: Metodo;
  valor: number;
  moeda: string;
  cliente: string;
  sucesso: boolean;
  idTransacao: string | null;
  mensagem: string;
  criadoEm: string;
}

export const METODO_LABEL: Record<Metodo, string> = {
  STRIPE: 'Stripe',
  MERCADO_PAGO: 'Mercado Pago',
  PIX: 'Pix'
};
