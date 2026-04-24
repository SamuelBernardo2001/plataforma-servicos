# Modelo de Domínio — Plataforma de Serviços

## Entidades

### UserModel
Representa um usuário do sistema.

| Campo | Tipo | Descrição |
|---|---|---|
| id | UUID | Identificador único |
| nome | String | Nome completo |
| email | String | Login único no sistema |
| senha | String | Senha (BCrypt no M6) |
| perfil | UserENUM | CLIENTE, PRESTADOR ou ADMIN |
| telefone | String | Contato |
| ativo | Boolean | Soft delete |
| criadoEm | LocalDateTime | Data de criação |
| atualizadoEm | LocalDateTime | Última atualização |

### CategoryModel
Representa uma categoria de serviço.

| Campo | Tipo | Descrição |
|---|---|---|
| id | UUID | Identificador único |
| nome | String | Nome único da categoria |
| descricao | String | Descrição |
| ativo | Boolean | Soft delete |
| criadoEm | LocalDateTime | Data de criação |
| atualizadoEm | LocalDateTime | Última atualização |

### ServiceModel
Representa um serviço cadastrado por um prestador.

| Campo | Tipo | Descrição |
|---|---|---|
| id | UUID | Identificador único |
| nome | String | Nome do serviço |
| descricao | String | Descrição detalhada |
| preco | BigDecimal | Valor do serviço |
| telefoneContato | String | Contato do prestador |
| prestador | UserModel | Dono do serviço (PRESTADOR) |
| categoria | CategoryModel | Categoria do serviço |
| ativo | Boolean | Soft delete |
| criadoEm | LocalDateTime | Data de criação |
| atualizadoEm | LocalDateTime | Última atualização |

### ServiceOrderModel
Representa uma contratação entre cliente e prestador.
É o coração do marketplace — sem ela não há avaliação.

| Campo | Tipo | Descrição |
|---|---|---|
| id | UUID | Identificador único |
| service | ServiceModel | Serviço contratado |
| cliente | UserModel | Quem contratou |
| prestador | UserModel | Quem executa |
| descricao | String | Detalhes do pedido |
| price | BigDecimal | Valor negociado |
| status | OrderStatusEnum | Estado atual da ordem |
| criadoEm | LocalDateTime | Data da contratação |
| atualizadoEm | LocalDateTime | Última atualização |
| concluidoEm | LocalDateTime | Data de conclusão |

**Fluxo de status:**

REQUESTED → ACCEPTED → COMPLETED
↘ CANCELED

### ReviewModel
Representa uma avaliação de serviço.
Só existe após ordem COMPLETED.

| Campo | Tipo | Descrição |
|---|---|---|
| id | UUID | Identificador único |
| service | ServiceModel | Serviço avaliado |
| usuario | UserModel | Cliente que avaliou |
| serviceOrder | ServiceOrderModel | Ordem que originou |
| classificacao | Integer | Nota de 1 a 5 |
| comentario | String | Texto da avaliação |
| editado | Boolean | Se foi editada |
| editadoEm | LocalDateTime | Quando foi editada |
| criadoEm | LocalDateTime | Data da avaliação |

### FavoriteModel
Representa um serviço favoritado por um usuário.

| Campo | Tipo | Descrição |
|---|---|---|
| id | UUID | Identificador único |
| usuario | UserModel | Quem favoritou |
| service | ServiceModel | Serviço favoritado |
| criadoEm | LocalDateTime | Data do favorito |

### ServiceImageModel
Representa uma imagem de um serviço.
Limite de 5 imagens por serviço.

| Campo | Tipo | Descrição |
|---|---|---|
| id | UUID | Identificador único |
| service | ServiceModel | Serviço relacionado |
| url | String | URL da imagem |

### ReportModel
Representa uma denúncia contra um usuário.

| Campo | Tipo | Descrição |
|---|---|---|
| id | UUID | Identificador único |
| reporter | UserModel | Quem denunciou |
| reportedUser | UserModel | Quem foi denunciado |
| serviceOrder | ServiceOrderModel | Ordem relacionada (opcional) |
| razao | String | Motivo da denúncia |
| descricao | String | Detalhes (mínimo 20 chars) |
| status | ReportStatusEnum | PENDENTE, RESOLVIDA, REJEITADA |
| criadoEm | LocalDateTime | Data da denúncia |

### MessageModel
Representa uma mensagem do chat interno.
Vinculada a uma ordem de serviço.

| Campo | Tipo | Descrição |
|---|---|---|
| id | UUID | Identificador único |
| serviceOrder | ServiceOrderModel | Ordem da conversa |
| remetente | UserModel | Quem enviou |
| receptor | UserModel | Quem recebe |
| conteudo | String | Texto da mensagem |
| ler | Boolean | Se foi lida |
| editado | Boolean | Se foi editada |
| editadoEm | LocalDateTime | Quando foi editada |
| enviadoEm | LocalDateTime | Data de envio |

### EnderecoModel
Representa o endereço de um usuário.
Visível ao prestador apenas com ordem ACCEPTED.

| Campo | Tipo | Descrição |
|---|---|---|
| id | UUID | Identificador único |
| user | UserModel | Dono do endereço |
| cep | String | CEP |
| logradouro | String | Rua/Avenida |
| numero | String | Número |
| complemento | String | Complemento (opcional) |
| bairro | String | Bairro |
| cidade | String | Cidade |
| estado | String | Estado (2 chars) |
| criadoEm | LocalDateTime | Data de criação |
| atualizadoEm | LocalDateTime | Última atualização |

## Relacionamentos

User (1) ──── (N) Service
User (1) ──── (N) ServiceOrder (como cliente)
User (1) ──── (N) ServiceOrder (como prestador)
User (1) ──── (1) Endereco
Service (1) ── (N) ServiceOrder
Service (1) ── (N) Review
Service (1) ── (N) Favorite
Service (1) ── (N) ServiceImage
ServiceOrder (1) ── (1) Review
ServiceOrder (1) ── (N) Message
ServiceOrder (1) ── (1) Report

