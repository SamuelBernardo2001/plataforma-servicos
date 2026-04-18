## Status do Projeto
🚧 Em desenvolvimento

---

### ✅ M2 – Camada de Domínio Concluída

Foi estruturada a base arquitetural do backend seguindo separação por camadas:

**Pacotes criados:**
- `controller` → Responsável por receber requisições HTTP
- `service` → Regras de negócio da aplicação
- `repository` → Camada de acesso a dados (preparada para JPA)
- `entity` → Modelos de domínio (modelo puro)
- `dto` → Objetos de transferência de dados
- `security` → Estrutura preparada para autenticação JWT
- `config` → Configurações gerais (CORS, beans futuros, etc.)
- `exception` → Tratamento global de erros (base preparada)
- `mapper` → Conversão entre Entity ↔ DTO
- `util` → Classes utilitárias e helpers

**📦 Entidades de Domínio Implementadas:**
- UserModel + UserENUM (CLIENTE, PRESTADOR, ADMIN)
- CategoryModel
- ServiceModel
- ServiceOrderModel + OrderStatusEnum (REQUESTED, ACCEPTED, COMPLETED, CANCELED)
- ReviewModel
- FavoriteModel
- ServiceImageModel
- ReportModel + ReportStatusEnum (PENDENTE, RESOLVIDA, REJEITADA)
- MessageModel
- EnderecoModel

---

### ✅ M3 – Persistência + Banco de Dados Concluída

Foi realizada a implementação completa da camada de persistência
do sistema, integrando o backend com banco de dados relacional.

**🐳 Banco de Dados:**
- Configuração do MySQL utilizando Docker
- Criação do ambiente isolado com docker-compose
- Banco `plataforma_servicos` inicializado com sucesso
- MySQL rodando na porta 3307

**🧩 Integração com Spring Boot:**
- Configuração do datasource no `application.properties`
- Conexão com banco utilizando HikariCP
- Projeto conectado com sucesso ao MySQL

**🗄️ JPA (Hibernate):**
- Todas as entidades convertidas para JPA com `@Entity`, `@Table`, `@Id`
- Relacionamentos mapeados com `@ManyToOne`, `@OneToMany`, `@OneToOne`
- Todos os relacionamentos usando `FetchType.LAZY`
- Enums salvos como texto com `@Enumerated(EnumType.STRING)`
- UUIDs como identificadores com `GenerationType.UUID`

**📁 Repositórios criados:**
- UserRepository
- CategoryRepository
- ServiceRepository
- ServiceImageRepository
- ServiceOrderRepository
- ReviewRepository
- FavoriteRepository
- ReportRepository
- MessageRepository
- EnderecoRepository

**🐘 Flyway (Migrations):**
- V1__create_table_users
- V2__create_table_categories
- V3__create_table_services
- V4__create_table_service_images
- V5__create_table_service_orders
- V6__create_table_reviews
- V7__create_table_favorites
- V8__create_table_enderecos

---

### ✅ M4 – Validação + DTOs + Service Layer Concluída

Foi implementada a camada de validação, transferência de dados
e regras de negócio completas do sistema.

**📦 DTOs criados como Java Records:**

*UserDTOS/*
- `UserRequestDTO` → cadastro com validações
- `UserResponseDTO` → retorno sem senha
- `UserUpdateDTO` → atualizar nome e telefone
- `UserPasswordDTO` → alterar senha com confirmação
- `UserLoginDTO` → autenticação

*ServiceDTOS/*
- `ServiceRequestDTO` → cadastro com validações
- `ServiceResponseDTO` → retorno completo

*CategoryDTOS/*
- `CategoryRequestDTO`
- `CategoryResponseDTO`

*ServiceOrderDTOS/*
- `ServiceOrderRequestDTO`
- `ServiceOrderResponseDTO`

*ReviewDTOS/*
- `ReviewRequestDTO` → com @Min(1) e @Max(5) no rating
- `ReviewResponseDTO` → com campos editado e editadoEm

*FavoriteDTOS/*
- `FavoriteRequestDTO`
- `FavoriteResponseDTO`

*ReportDTOS/*
- `ReportRequestDTO`
- `ReportResponseDTO` → com status da denúncia

*MessageDTOS/*
- `MessageRequestDTO`
- `MessageResponseDTO` → com campos editado e editadoEm

*EnderecoDTOS/*
- `EnderecoRequestDTO` → cadastro completo
- `EnderecoResponseDTO`
- `EnderecoPatchDTO` → edição parcial campo por campo

**🔄 Mappers criados:**
- UserMapper
- CategoryMapper
- ServiceMapper
- ServiceOrderMapper
- ReviewMapper
- FavoriteMapper
- ReportMapper
- MessageMapper
- EnderecoMapper

**⚙️ Services criados com regras de negócio:**

- `UserService` → buscar, criar, atualizar, trocar senha, desativar
- `ServiceService` → listar (público/prestador), criar só PRESTADOR,
  atualizar, desativar, calcular média de avaliações
- `OrderService` → criar ordem, fluxo completo de status,
  listar por cliente/prestador, validar ordem concluída para avaliação
- `ReviewService` → avaliar só após COMPLETED, sem duplicata,
  editar, deletar, ADMIN modera
- `FavoriteService` → toggle com unicidade, verificar favorito,
  impede favoritar próprio serviço
- `CategoryService` → CRUD completo apenas ADMIN,
  unicidade de nome, serviços ficam pendentes ao desativar
- `EnderecoService` → cadastrar após conta criada, atualização
  completa e parcial, privacidade do endereço do cliente
  (prestador vê só com ordem ACCEPTED, some após COMPLETED)
- `ReportService` → criar denúncia, ADMIN resolve ou rejeita,
  filtrar por status
- `MessageService` → chat paginado (20 por página) igual WhatsApp,
  enviar, editar mensagem, marcar como lida

**🔒 Regras de negócio implementadas:**
- Apenas PRESTADOR pode cadastrar serviços
- Apenas CLIENTE pode criar ordens de serviço
- Cliente não pode contratar seu próprio serviço
- Avaliação só após ordem COMPLETED
- Um cliente avalia um serviço apenas uma vez
- Endereço do cliente visível ao prestador só com ordem ACCEPTED
- Endereço some após ordem COMPLETED — privacidade
- Soft delete em usuários, serviços e categorias
- Toggle de favoritos com unicidade
- Apenas ADMIN cria, edita e desativa categorias
- Apenas ADMIN modera denúncias e avaliações

---

### 🔜 Próximo Módulo
**M5 – API REST** — Controllers + endpoints padronizados
com `ApiResponse`, paginação obrigatória e versionamento `/api/`

### ✅ M5 — API REST
- `ApiResponse<T>` padronizado em todos os endpoints
- `GlobalExceptionHandler` para tratamento centralizado de erros
- `PaginatedResponse<T>` em todas as listagens
- Versionamento `/api/` em todos os endpoints
- `@PageableDefault(size = 20)` como padrão global

**Controllers criados:**
- `UserController` → cadastro, login, perfil, senha, desativar
- `ServiceController` → CRUD, painel do prestador, média de avaliações
- `CategoryController` → CRUD completo (apenas ADMIN)
- `ServiceOrderController` → fluxo completo de status
- `ReviewController` → avaliação pós COMPLETED, edição, moderação
- `FavoriteController` → toggle com unicidade
- `EnderecoController` → CRUD com edição parcial e privacidade
- `ReportController` → denúncias com moderação ADMIN
- `MessageController` → chat paginado com edição
- `ServiceImageController` → galeria com limite de 5 por serviço

**Migrations adicionais:**
- V9__create_table_reports
- V10__create_table_messages
- V11__alter_table_reviews_add_editado
- V12__alter_table_service_images

---

### 🔜 Próximo Módulo
**M6 – Segurança JWT** — Spring Security + JWT +
Refresh Token + controle de acesso por roles
(CLIENTE, PRESTADOR, ADMIN)