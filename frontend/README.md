# Frontend Angular

## Requisitos
- Node.js 20+
- npm 10+

## Executando backend + EJB (forma correta)
> Execute os comandos abaixo na **raiz do projeto** (`bip-teste-integrado`).
> Nao rode `spring-boot:run` no POM pai com `-am`, pois o modulo pai nao possui classe `main`.

## Execucao
```bash
mvn -pl ejb-module -am clean install -DskipTests
mvn -pl backend-module spring-boot:run
```

Alternativa (se estiver dentro de `backend-module`):
```bash
mvn -f ..\pom.xml -pl ejb-module -am install -DskipTests
mvn spring-boot:run
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
  - executar com: `mvn -pl backend-module -am test`
- Frontend:
  - `app.component.spec.ts`
  - `beneficio-api.service.spec.ts`
  - executar com: `cd frontend && npm test -- --watch=false --browsers=ChromeHeadless`

## Banco de dados
Scripts oficiais do desafio:
- `db/schema.sql`
- `db/seed.sql`

O backend usa H2 em memoria com inicializacao automatica por:
- `backend-module/src/main/resources/schema.sql`
- `backend-module/src/main/resources/data.sql`

## CI
Workflow em `.github/workflows/ci.yml` executa build e testes Maven dos modulos Java.
A aplicacao sobe em `http://localhost:4200` e consome a API em `http://localhost:8080`.
