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

Foi realizada a implementação completa da camada de persistência do sistema, integrando o backend com banco de dados relacional.

🐳 Banco de Dados

Configuração do MySQL utilizando Docker

Criação do ambiente isolado com docker-compose

Banco plataforma_servicos inicializado com sucesso

🧩 Integração com Spring Boot

Configuração do datasource no application.properties

Conexão com banco utilizando HikariCP

Projeto conectado com sucesso ao MySQL

🗄️ JPA (Hibernate)

Conversão parcial das entidades para JPA:

UserModel → @Entity, @Table, @Id

ServiceModel → @Entity, @Table, @Id

✔ Uso de jakarta.persistence
✔ Mapeamento de colunas com @Column
✔ Estrutura preparada para relacionamentos futuros

📁 Repositórios

Criação dos repositórios com Spring Data JPA:

UserRepository

ServiceRepository

🐘 Flyway (Migrations)

Criação do versionamento do banco de dados com Flyway.

Migrations criadas:

V1__create_table_users

V2__create_table_categories

V3__create_table_services

V4__create_table_service_images

V5__create_table_service_orders

V6__create_table_reviews

V7__create_table_favorites

✔ Execução automática ao subir o projeto
✔ Controle de versão do banco ativo
✔ Histórico salvo em flyway_schema_history

🧪 Resultado

✔ Projeto compila com sucesso (BUILD SUCCESS)

✔ Spring Boot inicia corretamente

✔ Flyway executa as migrations

✔ 7 tabelas criadas no banco

✔ Integração com banco funcionando

⚠️ Observações

IDs estão sendo armazenados como UUID (BINARY(16))

Migrations não devem ser alteradas após execução

Novas alterações devem ser feitas em novas versões (V8, V9...)