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

M2 – Camada de Domínio Concluída

Foi estruturada a base arquitetural do backend seguindo separação por camadas:

Pacotes criados:

controller → Responsável por receber requisições HTTP

service → Regras de negócio da aplicação

repository → Camada de acesso a dados (preparada para JPA)

entity → Modelos de domínio (modelo puro)

dto → Objetos de transferência de dados

security → Estrutura preparada para autenticação JWT

config → Configurações gerais (CORS, beans futuros, etc.)

exception → Tratamento global de erros (base preparada)

mapper → Conversão entre Entity ↔ DTO

util → Classes utilitárias e helpers

📦 Entidades de Domínio Implementadas

Modelagem completa do núcleo do sistema:

User

Category

Service

ServiceOrder

Review

Favorite

ServiceImage

Report

Message

OrderStatus (enum)

Nesta etapa foi criado o modelo de domínio puro, sem dependência de JPA, garantindo desacoplamento e base sólida para a próxima fase.

🔜 Próximo Módulo

M3 – Integração com banco de dados utilizando Spring Data JPA.