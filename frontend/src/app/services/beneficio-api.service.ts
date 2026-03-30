import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Beneficio, BeneficioPayload, TransferenciaPayload } from '../models/beneficio.model';

@Injectable({ providedIn: 'root' })
export class BeneficioApiService {
  private readonly baseUrl = `${environment.apiUrl}/api/v1/beneficios`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.baseUrl);
  }

  create(payload: BeneficioPayload): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.baseUrl, payload);
  }

  update(id: number, payload: BeneficioPayload): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.baseUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  transfer(payload: TransferenciaPayload): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/transferencias`, payload);
  }
}
