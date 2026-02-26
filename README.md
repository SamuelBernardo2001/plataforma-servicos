# Plataforma de Serviços

Marketplace de serviços locais que conecta prestadores (construção, barbearia, elétrica, etc.) a clientes com contratação, avaliações e histórico real.

## Tecnologias

**Backend**
- Java 17 + Spring Boot 3.2
- MySQL + Flyway
- Spring Security + JWT

**Frontend**
- Angular 17
- TypeScript
- SCSS

**Infraestrutura**
- Docker + Docker Compose
- GitHub Actions (CI/CD)

## Estrutura do Projeto
```
plataforma-servicos/
├── backend-spring/     # API REST Java/Spring Boot
├── frontend-angular/   # App Angular
├── database/           # Migrations e seeds
├── docker/             # Configurações Docker
├── docs/               # Documentação
└── .github/workflows/  # CI/CD
```

## Como rodar localmente

### Pré-requisitos
- Java 17+
- Node.js 18+
- Docker

### Backend
```bash
cd backend-spring
mvn spring-boot:run
```

### Frontend
```bash
cd frontend-angular/plataforma
ng serve
```

## Status do Projeto
🚧 Em desenvolvimento