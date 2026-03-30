# Desafio Fullstack Integrado - Solucao

## Visao geral
Esta entrega implementa uma arquitetura em camadas com:
- `db`: scripts SQL de schema e seed
- `ejb-module`: regra de negocio de transferencia com validacoes, lock e rollback
- `backend-module`: API Spring Boot (CRUD + transferencia + Swagger)
- `frontend`: aplicacao Angular consumindo a API

## Arquitetura
1. Entidade `Beneficio` no modulo EJB com `@Version` para optimistic locking.
2. `BeneficioEjbService` aplica as regras de transferencia:
   - valida parametros
   - impede origem e destino iguais
   - impede transferencia com saldo insuficiente
   - bloqueia registros com `PESSIMISTIC_WRITE` em ordem deterministica
   - garante rollback em excecao de negocio
3. Backend Spring expoe endpoints REST e delega transferencia para o EJB.
4. Frontend Angular oferece telas/formularios para CRUD e transferencia.

## Requisitos
- Java 17
- Maven 3.9+
- Node 20+ e npm 10+

## Executando backend + EJB
```bash
mvn clean verify
mvn -pl backend-module spring-boot:run
```

API base: `http://localhost:8080/api/v1/beneficios`

Swagger UI: `http://localhost:8080/swagger-ui.html`

H2 console: `http://localhost:8080/h2-console`

## Executando frontend
```bash
cd frontend
npm install
npm start
```

Frontend: `http://localhost:4200`

## Endpoints principais
- `GET /api/v1/beneficios`
- `GET /api/v1/beneficios/{id}`
- `POST /api/v1/beneficios`
- `PUT /api/v1/beneficios/{id}`
- `DELETE /api/v1/beneficios/{id}`
- `POST /api/v1/beneficios/transferencias`

Exemplo de transferencia:
```json
{
  "fromId": 1,
  "toId": 2,
  "amount": 100.00
}
```

## Testes
- Backend:
  - `BeneficioServiceIntegrationTest`
  - `BeneficioControllerIntegrationTest`
- Frontend:
  - `app.component.spec.ts`
  - `beneficio-api.service.spec.ts`

## Banco de dados
Scripts oficiais do desafio:
- `db/schema.sql`
- `db/seed.sql`

O backend usa H2 em memoria com inicializacao automatica por:
- `backend-module/src/main/resources/schema.sql`
- `backend-module/src/main/resources/data.sql`

## CI
Workflow em `.github/workflows/ci.yml` executa build e testes Maven dos modulos Java.
ps: CI não funcionou por ser uma ferramenta paga
