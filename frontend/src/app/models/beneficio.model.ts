export interface Beneficio {
  id: number;
  nome: string;
  descricao?: string;
  valor: number;
  ativo: boolean;
  version: number;
}

export interface BeneficioPayload {
  nome: string;
  descricao: string;
  valor: number;
  ativo: boolean;
}

export interface TransferenciaPayload {
  fromId: number;
  toId: number;
  amount: number;
}
