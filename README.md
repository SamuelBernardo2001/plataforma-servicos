## Como rodar localmente

### Pré-requisitos
- Java 21+
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

## Roadmap

| Milestone | Descrição | Status |
|---|---|---|
| M1 | Fundação do Projeto | ✅ Concluído |
| M2 | Camada de Domínio | ✅ Concluído |
| M3 | Persistência + Banco de Dados | ✅ Concluído |
| M4 | Validação + DTOs + Service Layer | ✅ Concluído |
| M5 | API REST | 🔄 Em andamento |
| M6 | Segurança JWT | ⏳ Pendente |
| M7 | Core do Marketplace | ⏳ Pendente |
| M8 | Observabilidade e Governança | ⏳ Pendente |
| M9 | Frontend Angular | ⏳ Pendente |
| M10 | Produção | ⏳ Pendente |

## Status do Projeto

🔄 Em andamento — M5 (API REST)

### ✅ M1 — Fundação do Projeto
- Estrutura de pastas do monorepo
- Projeto Spring Boot configurado
- Pacotes criados: `controller`, `service`, `repository`, `entity`, `dto`, `security`, `config`, `exception`, `mapper`, `util`

### ✅ M2 — Camada de Domínio
Entidades modeladas como domínio puro (sem JPA):
- `UserModel` + `UserENUM` (CLIENTE, PRESTADOR, ADMIN)
- `CategoryModel`
- `ServiceModel`
- `ServiceOrderModel` + `OrderStatusEnum` (REQUESTED, ACCEPTED, COMPLETED, CANCELED)
- `ReviewModel`
- `FavoriteModel`
- `ServiceImageModel`
- `ReportModel` + `ReportStatusEnum` (PENDENTE, RESOLVIDA, REJEITADA)
- `MessageModel`
- `EnderecoModel`

### ✅ M3 — Persistência + Banco de Dados
- MySQL 8 via Docker na porta 3307
- Todas as entidades convertidas para JPA com relacionamentos `@ManyToOne`, `@OneToMany`, `@OneToOne`
- 10 repositories criados com Spring Data JPA
- 8 migrations Flyway (V1 a V8)

### ✅ M4 — Validação + DTOs + Service Layer
- DTOs criados como Java Records com validações (`@NotBlank`, `@Email`, `@Size`, `@Min`, `@Max`)
- Mappers criados para todas as entidades
- 9 Services com regras de negócio completas:
    - `UserService`, `ServiceService`, `OrderService`, `ReviewService`
    - `FavoriteService`, `CategoryService`, `EnderecoService`
    - `ReportService`, `MessageService`

**Principais regras implementadas:**
- Apenas PRESTADOR pode cadastrar serviços
- Apenas CLIENTE pode criar ordens de serviço
- Avaliação só após ordem COMPLETED
- Endereço do cliente visível ao prestador só com ordem ACCEPTED
- Toggle de favoritos com unicidade
- Moderação de denúncias e avaliações pelo ADMIN

### 🔄 M5 — API REST (em andamento)
- `ApiResponse` padronizado para todas as respostas
- `GlobalExceptionHandler` para tratamento centralizado de erros
- `SecurityConfig` temporária liberando endpoints para testes