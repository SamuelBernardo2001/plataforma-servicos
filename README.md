# Plataforma de Serviços

Marketplace de serviços locais que conecta prestadores (construção, barbearia, elétrica, etc.) a clientes com contratação, avaliações e histórico real.

## Tecnologias

**Backend**
- Java 21 + Spring Boot 3.2
- MySQL 8 + Flyway
- Spring Security + JWT

**Frontend**
- Angular 17
- TypeScript
- SCSS

**Infraestrutura**
- Docker + Docker Compose
- GitHub Actions (CI/CD)

## Estrutura do Projeto

plataforma-servicos/
├── backend-spring/     # API REST Java/Spring Boot
├── frontend-angular/   # App Angular
├── database/           # Migrations e seeds
├── docker/             # Configurações Docker
├── docs/
│   ├── api/            # Documentação dos endpoints REST
│   └── arquitetura/    # Diagramas e decisões arquiteturais
└── .github/workflows/  # CI/CD

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
| M5 | API REST | ✅ Concluído |
| M6 | Segurança JWT | ⏳ Pendente |
| M7 | Core do Marketplace | ⏳ Pendente |
| M8 | Observabilidade e Governança | ⏳ Pendente |
| M9 | Frontend Angular | ⏳ Pendente |
| M10 | Produção | ⏳ Pendente |

## Status do Projeto

---

### ✅ M1 — Fundação do Projeto
- Estrutura de pastas do monorepo criada
- Projeto Spring Boot configurado com dependências iniciais
- Pacotes criados: `controller`, `service`, `repository`, `entity`,
  `dto`, `security`, `config`, `exception`, `mapper`, `util`

---

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

---

### ✅ M3 — Persistência + Banco de Dados
- MySQL 8 via Docker na porta 3307
- Todas as entidades convertidas para JPA com `@Entity`, `@Table`, `@Id`
- Relacionamentos mapeados com `@ManyToOne`, `@OneToMany`, `@OneToOne`
- Todos os relacionamentos usando `FetchType.LAZY`
- Enums salvos como texto com `@Enumerated(EnumType.STRING)`
- UUIDs como identificadores com `GenerationType.UUID`
- 10 repositories criados com Spring Data JPA
- 13 migrations Flyway (V1 a V13)

---

### ✅ M4 — Validação + DTOs + Service Layer
DTOs criados como Java Records com validações (`@NotBlank`, `@Email`, `@Size`, `@Min`, `@Max`):
- `UserDTOS` → UserRequestDTO, UserResponseDTO, UserUpdateDTO, UserPasswordDTO, UserLoginDTO
- `ServiceDTOS` → ServiceRequestDTO, ServiceResponseDTO
- `ServiceImageDTOS` → ServiceImageRequestDTO, ServiceImageResponseDTO
- `CategoryDTOS` → CategoryRequestDTO, CategoryResponseDTO
- `ServiceOrderDTOS` → ServiceOrderRequestDTO, ServiceOrderResponseDTO
- `ReviewDTOS` → ReviewRequestDTO, ReviewResponseDTO
- `FavoriteDTOS` → FavoriteRequestDTO, FavoriteResponseDTO
- `ReportDTOS` → ReportRequestDTO, ReportResponseDTO
- `MessageDTOS` → MessageRequestDTO, MessageResponseDTO
- `EnderecoDTOS` → EnderecoRequestDTO, EnderecoResponseDTO, EnderecoPatchDTO

Mappers criados para todas as entidades:
- UserMapper, CategoryMapper, ServiceMapper, ServiceImageMapper,
  ServiceOrderMapper, ReviewMapper, FavoriteMapper,
  ReportMapper, MessageMapper, EnderecoMapper

10 Services com regras de negócio completas:
- `UserService` → buscar, criar, atualizar, trocar senha, desativar
- `ServiceService` → listar público/prestador, criar só PRESTADOR, atualizar, desativar
- `ServiceImageService` → adicionar imagem, listar, remover (limite de 5 por serviço)
- `OrderService` → criar ordem, fluxo completo de status, listar por cliente/prestador
- `ReviewService` → avaliar só após COMPLETED, sem duplicata, editar, deletar, ADMIN modera
- `FavoriteService` → toggle com unicidade, verificar favorito
- `CategoryService` → CRUD completo apenas ADMIN, unicidade de nome
- `EnderecoService` → CRUD com edição parcial, privacidade do endereço
- `ReportService` → criar denúncia, ADMIN resolve ou rejeita, filtrar por status
- `MessageService` → chat paginado, enviar, editar mensagem, marcar como lida

**Principais regras de negócio:**
- Apenas PRESTADOR pode cadastrar serviços
- Apenas CLIENTE pode criar ordens de serviço
- Cliente não pode contratar seu próprio serviço
- Avaliação só após ordem COMPLETED
- Um cliente avalia um serviço apenas uma vez
- Endereço do cliente visível ao prestador só com ordem ACCEPTED
- Endereço some após ordem COMPLETED — privacidade garantida
- Soft delete em usuários, serviços e categorias
- Toggle de favoritos com unicidade
- Apenas ADMIN cria, edita e desativa categorias
- Apenas ADMIN modera denúncias e avaliações
- Limite de 10 imagens por serviço

---

### ✅ M5 — API REST
- `ApiResponse<T>` padronizado em todos os endpoints
- `GlobalExceptionHandler` com 5 handlers de erro
- `PaginatedResponse<T>` em todas as listagens
- `@PageableDefault(size = 20)` como padrão global

10 Controllers criados:
- `UserController` → cadastro, login, perfil, senha, desativar
- `ServiceController` → CRUD, painel do prestador, média de avaliações
- `ServiceImageController` → galeria com limite de 5 por serviço
- `CategoryController` → CRUD completo (apenas ADMIN)
- `ServiceOrderController` → fluxo completo de status de contratação
- `ReviewController` → avaliação pós COMPLETED, edição, moderação ADMIN
- `FavoriteController` → toggle com unicidade, verificação de favorito
- `EnderecoController` → CRUD com edição parcial e privacidade
- `ReportController` → denúncias com moderação ADMIN
- `MessageController` → chat paginado com edição de mensagens

Migrations adicionais criadas:
- V9__create_table_reports
- V10__create_table_messages
- V11__alter_table_reviews_add_editado
- V12__alter_table_service_images

---

### ⏳ M6 — Segurança JWT
Spring Security + JWT + Refresh Token + controle por roles

### ⏳ M7 — Core do Marketplace
Funcionalidades avançadas do marketplace

### ⏳ M8 — Observabilidade e Governança
Auditoria, logs estruturados, Actuator, Swagger

### ⏳ M9 — Frontend Angular
Interface Angular 17 com integração completa

### ⏳ M10 — Produção
Docker completo, CI/CD, deploy, testes automatizados