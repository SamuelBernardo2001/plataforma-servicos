# Fluxos de Negócio — Plataforma de Serviços

## Fluxo de Contratação

1. Cliente busca serviços ativos
2. Cliente cria ordem (status: REQUESTED)
3. Prestador aceita (status: ACCEPTED)
   → Endereço do cliente fica visível ao prestador
4. Prestador conclui (status: COMPLETED)
   → Endereço do cliente some — privacidade
   → Avaliação liberada para o cliente
5. Cliente avalia o serviço (1 a 5 estrelas)

## Fluxo de Avaliação

- Avaliação só existe após ordem COMPLETED
- Um cliente avalia um serviço apenas uma vez
- Cliente pode editar ou deletar sua avaliação
- ADMIN pode remover avaliações inadequadas

## Fluxo de Denúncia

1. Usuário cria denúncia (status: PENDENTE)
2. ADMIN analisa
3. ADMIN resolve → providências tomadas
   ADMIN rejeita → denúncia inválida

## Fluxo de Endereço

- Endereço cadastrado após criação da conta
- Não é obrigatório no cadastro
- Visível ao prestador APENAS com ordem ACCEPTED
- Some após ordem COMPLETED — privacidade garantida

## Soft Delete

Usuários, serviços e categorias nunca são deletados.
O campo `ativo = false` os remove das listagens.
Histórico de ordens e avaliações é mantido.