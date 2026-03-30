# Desafio Fullstack Integrado

## Objetivo
Entregar uma aplicacao completa em camadas com:
- scripts de banco
- correcao do bug no EJB de transferencia
- backend Spring Boot com CRUD e integracao com EJB
- frontend Angular consumindo o backend
- testes automatizados
- documentacao da API

## Checklist de entrega
1. Executar `db/schema.sql` e `db/seed.sql` no banco alvo (ou usar inicializacao automatica do backend com H2).
2. Validar as regras da transferencia:
   - saldo suficiente
   - origem e destino obrigatorios e diferentes
   - bloqueio para concorrencia
   - rollback em caso de erro
3. Testar endpoints CRUD e transferencia.
4. Rodar o frontend Angular.
5. Revisar Swagger em `/swagger-ui.html`.

## Criterios de avaliacao cobertos
- Arquitetura em camadas
- Correcao do EJB
- CRUD + transferencia
- Qualidade e organizacao do codigo
- Testes
- Documentacao
- Frontend funcional
