import { CommonModule, DecimalPipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { Beneficio, BeneficioPayload, TransferenciaPayload } from './models/beneficio.model';
import { BeneficioApiService } from './services/beneficio-api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, DecimalPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  private readonly beneficioApi = inject(BeneficioApiService);

  beneficios: Beneficio[] = [];
  loading = false;
  message = '';
  error = '';
  editingId: number | null = null;

  readonly beneficioForm = this.formBuilder.nonNullable.group({
    nome: ['', [Validators.required, Validators.maxLength(100)]],
    descricao: ['', [Validators.maxLength(255)]],
    valor: [0, [Validators.required, Validators.min(0)]],
    ativo: [true, [Validators.required]]
  });

  readonly transferenciaForm = this.formBuilder.nonNullable.group({
    fromId: [0, [Validators.required]],
    toId: [0, [Validators.required]],
    amount: [0, [Validators.required, Validators.min(0.01)]]
  });

  ngOnInit(): void {
    this.loadBeneficios();
  }

  submitBeneficio(): void {
    if (this.beneficioForm.invalid) {
      this.error = 'Preencha os campos obrigatorios do beneficio.';
      return;
    }

    this.clearAlerts();
    const payload = this.toBeneficioPayload();
    this.loading = true;

    const request$ = this.editingId === null
      ? this.beneficioApi.create(payload)
      : this.beneficioApi.update(this.editingId, payload);

    request$
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.message = this.editingId === null
            ? 'Beneficio criado com sucesso.'
            : 'Beneficio atualizado com sucesso.';
          this.cancelEdit();
          this.loadBeneficios();
        },
        error: (err: any) => {
          this.error = err?.error?.message ?? 'Falha ao salvar beneficio.';
        }
      });
  }

  editBeneficio(beneficio: Beneficio): void {
    this.editingId = beneficio.id;
    this.beneficioForm.setValue({
      nome: beneficio.nome,
      descricao: beneficio.descricao ?? '',
      valor: beneficio.valor,
      ativo: beneficio.ativo
    });
    this.clearAlerts();
  }

  cancelEdit(): void {
    this.editingId = null;
    this.beneficioForm.reset({
      nome: '',
      descricao: '',
      valor: 0,
      ativo: true
    });
  }

  removeBeneficio(id: number): void {
    this.clearAlerts();
    this.loading = true;
    this.beneficioApi.delete(id)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.message = 'Beneficio removido com sucesso.';
          this.loadBeneficios();
        },
        error: (err: any) => {
          this.error = err?.error?.message ?? 'Falha ao remover beneficio.';
        }
      });
  }

  submitTransferencia(): void {
    if (this.transferenciaForm.invalid) {
      this.error = 'Preencha os campos obrigatorios da transferencia.';
      return;
    }

    this.clearAlerts();
    this.loading = true;
    const payload = this.toTransferenciaPayload();

    this.beneficioApi.transfer(payload)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
          this.message = 'Transferencia realizada com sucesso.';
          this.transferenciaForm.reset({
            fromId: 0,
            toId: 0,
            amount: 0
          });
          this.loadBeneficios();
        },
        error: (err: any) => {
          this.error = err?.error?.message ?? 'Falha ao transferir.';
        }
      });
  }

  private loadBeneficios(): void {
    this.loading = true;
    this.beneficioApi.list()
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (data) => {
          this.beneficios = data;
        },
        error: (err: any) => {
          this.error = err?.error?.message ?? 'Falha ao carregar beneficios.';
        }
      });
  }

  private toBeneficioPayload(): BeneficioPayload {
    const raw = this.beneficioForm.getRawValue();
    return {
      nome: raw.nome.trim(),
      descricao: raw.descricao.trim(),
      valor: Number(raw.valor),
      ativo: raw.ativo
    };
  }

  private toTransferenciaPayload(): TransferenciaPayload {
    const raw = this.transferenciaForm.getRawValue();
    return {
      fromId: Number(raw.fromId),
      toId: Number(raw.toId),
      amount: Number(raw.amount)
    };
  }

  private clearAlerts(): void {
    this.message = '';
    this.error = '';
  }
}
