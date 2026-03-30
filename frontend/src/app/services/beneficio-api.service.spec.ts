import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { BeneficioApiService } from './beneficio-api.service';

describe('BeneficioApiService', () => {
  let service: BeneficioApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BeneficioApiService, provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(BeneficioApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should call list endpoint', () => {
    service.list().subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/v1/beneficios');
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });
});
