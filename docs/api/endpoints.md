# Endpoints da API — Plataforma de Serviços

Base URL: `http://localhost:8080/api`

> Documentação interativa disponível em:
> http://localhost:8080/swagger-ui.html

## Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| POST | /users/register | Cadastrar usuário |
| POST | /users/login | Autenticar |
| GET | /users | Listar ativos |
| GET | /users/{id} | Buscar por ID |
| PUT | /users/{id}/profile | Atualizar perfil |
| PUT | /users/{id}/password | Alterar senha |
| DELETE | /users/{id} | Desativar |

## Serviços

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /services | Listar ativos |
| GET | /services/{id} | Buscar por ID |
| GET | /services/category/{id} | Listar por categoria |
| GET | /services/prestador/{id} | Perfil público prestador |
| GET | /services/meus/{id} | Painel do prestador |
| POST | /services/prestador/{id} | Criar serviço |
| PUT | /services/{id}/prestador/{id} | Atualizar |
| DELETE | /services/{id}/prestador/{id} | Desativar |
| GET | /services/{id}/media-avaliacao | Média de avaliações |

## Categorias

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /categories | Listar ativas |
| GET | /categories/{id} | Buscar por ID |
| POST | /categories/admin/{id} | Criar (ADMIN) |
| PUT | /categories/{id}/admin/{id} | Atualizar (ADMIN) |
| DELETE | /categories/{id}/admin/{id} | Desativar (ADMIN) |

## Ordens de Serviço

| Método | Endpoint | Descrição |
|---|---|---|
| POST | /service-orders/cliente/{id} | Criar ordem |
| GET | /service-orders/{id}/usuario/{id} | Buscar por ID |
| GET | /service-orders/cliente/{id} | Ordens do cliente |
| GET | /service-orders/prestador/{id} | Ordens do prestador |
| GET | /service-orders/cliente/{id}/status/{status} | Filtrar por status |
| PATCH | /service-orders/{id}/status/usuario/{id} | Atualizar status |
| GET | /service-orders/verificar-concluida | Verificar ordem concluída |

## Avaliações

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /reviews/service/{id} | Listar do serviço |
| POST | /reviews/cliente/{id} | Avaliar |
| PUT | /reviews/{id}/cliente/{id} | Editar |
| DELETE | /reviews/{id}/cliente/{id} | Deletar |
| DELETE | /reviews/{id}/admin/{id} | Remover (ADMIN) |

## Favoritos

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /favorites/usuario/{id} | Listar favoritos |
| GET | /favorites/usuario/{id}/service/{id} | Verificar favorito |
| POST | /favorites/toggle/usuario/{id}/service/{id} | Toggle |

## Endereços

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /enderecos/usuario/{id} | Buscar endereço |
| GET | /enderecos/cliente/{id}/prestador/{id} | Ver endereço (ACCEPTED) |
| POST | /enderecos/usuario/{id} | Cadastrar |
| PUT | /enderecos/usuario/{id} | Atualizar completo |
| PATCH | /enderecos/usuario/{id} | Atualizar parcial |
| DELETE | /enderecos/usuario/{id} | Deletar |

## Denúncias

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /reports/admin/{id} | Listar (ADMIN) |
| GET | /reports/admin/{id}/status/{status} | Filtrar por status (ADMIN) |
| POST | /reports/usuario/{id} | Criar denúncia |
| PATCH | /reports/{id}/resolver/admin/{id} | Resolver (ADMIN) |
| PATCH | /reports/{id}/rejeitar/admin/{id} | Rejeitar (ADMIN) |

## Mensagens

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /messages/ordem/{id}/usuario/{id} | Listar mensagens |
| POST | /messages/remetente/{id} | Enviar mensagem |
| PATCH | /messages/{id}/remetente/{id} | Editar mensagem |
| PATCH | /messages/ordem/{id}/lidas/usuario/{id} | Marcar como lidas |

## Imagens de Serviço

| Método | Endpoint | Descrição |
|---|---|---|
| GET | /service-images/service/{id} | Listar imagens |
| POST | /service-images/service/{id}/prestador/{id} | Adicionar imagem |
| DELETE | /service-images/{id}/prestador/{id} | Remover imagem |