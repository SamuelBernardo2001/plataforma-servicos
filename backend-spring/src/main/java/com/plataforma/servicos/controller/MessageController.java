package com.plataforma.servicos.controller;

import com.plataforma.servicos.dto.messageDTOS.MessageRequestDTO;
import com.plataforma.servicos.dto.messageDTOS.MessageResponseDTO;
import com.plataforma.servicos.exception.ApiResponse;
import com.plataforma.servicos.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// @RestController → combina @Controller + @ResponseBody
// Todos os métodos retornam JSON automaticamente
@RestController

// /api/messages → prefixo de todos os endpoints desta classe
@RequestMapping("/api/messages")

// @RequiredArgsConstructor → injeta MessageService via construtor
// Padrão recomendado pelo Spring — mais seguro que @Autowired
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // CONSULTAS
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    // GET /api/messages/ordem/{ordemId}/usuario/{usuarioId}
    // Lista mensagens de uma ordem com paginação
    // Regra: apenas cliente ou prestador da ordem podem ver
    // Regra: 20 mensagens por página — igual ao WhatsApp
    //        que carrega mensagens aos poucos quando
    //        o usuário sobe o scroll
    // Regra: ordenadas das mais antigas para mais recentes
    //        para manter a ordem cronológica do chat
    // Por que paginação?
    //   Uma conversa pode ter centenas de mensagens
    //   Carregar tudo de uma vez sobrecarrega banco e frontend
    //   Paginação carrega apenas o necessário
    // @RequestParam pagina → qual página carregar
    //   pagina=0 → mensagens mais recentes
    //   pagina=1 → mensagens anteriores (scroll up)
    // No M7 o usuarioId virá do token JWT automaticamente
    @GetMapping("/ordem/{ordemId}/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<Page<MessageResponseDTO>>> findByOrdem(
            @PathVariable UUID ordemId,
            @PathVariable UUID usuarioId,
            @RequestParam(defaultValue = "0") int pagina) {
        Page<MessageResponseDTO> messages = messageService.findByOrdem(
                ordemId, usuarioId, pagina);
        return ResponseEntity.ok(
                ApiResponse.success(
                        messages,
                        "Mensagens listadas com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // ENVIO DE MENSAGEM

    // POST /api/messages/remetente/{remetenteId}
    // Envia mensagem vinculada a uma ordem
    // Regra: apenas cliente ou prestador da ordem podem enviar
    // Regra: não é possível enviar em ordem COMPLETED ou CANCELED
    //        faz sentido — se o serviço foi concluído ou cancelado
    //        não há mais o que conversar nessa ordem
    // Regra: receptor é definido automaticamente pelo Service
    //        se remetente é cliente → receptor é prestador
    //        se remetente é prestador → receptor é cliente
    //        não precisa informar o receptor no body
    // No M7 o remetenteId virá do token JWT automaticamente
    @PostMapping("/remetente/{remetenteId}")
    public ResponseEntity<ApiResponse<MessageResponseDTO>> send(
            @PathVariable UUID remetenteId,
            @Valid @RequestBody MessageRequestDTO dto) {
        MessageResponseDTO message = messageService.send(remetenteId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        message,
                        "Mensagem enviada com sucesso",
                        HttpStatus.CREATED.value()
                ));
    }

    // EDIÇÃO DE MENSAGEM

    // PATCH /api/messages/{id}/remetente/{remetenteId}
    // Edita conteúdo de uma mensagem
    // Regra: apenas o remetente pode editar sua própria mensagem
    // Regra: mensagem não pode ser deletada — apenas editada
    //        mantém histórico íntegro da conversa
    // Regra: marca editado = true e registra editadoEm
    //        igual ao WhatsApp que mostra "editada"
    //        receptor sabe que mensagem foi modificada
    // Regra: não é possível editar em ordem finalizada
    // Por que PATCH e não PUT?
    //   PUT → substitui o recurso inteiro
    //   PATCH → atualiza apenas o conteúdo da mensagem
    // Por que @RequestParam e não @RequestBody?
    //   Editar mensagem é simples — apenas o texto muda
    //   @RequestParam evita criar um DTO só para isso
    // No M7 o remetenteId virá do token JWT automaticamente
    @PatchMapping("/{id}/remetente/{remetenteId}")
    public ResponseEntity<ApiResponse<MessageResponseDTO>> edit(
            @PathVariable UUID id,
            @PathVariable UUID remetenteId,
            @RequestParam String novoConteudo) {
        MessageResponseDTO message = messageService.edit(id, remetenteId, novoConteudo);
        return ResponseEntity.ok(
                ApiResponse.success(
                        message,
                        "Mensagem editada com sucesso",
                        HttpStatus.OK.value()
                ));
    }

    // CONTROLE DE LEITURA

    // PATCH /api/messages/ordem/{ordemId}/lidas/usuario/{usuarioId}
    // Marca todas as mensagens não lidas como lidas
    // Regra: apenas o receptor pode marcar como lida
    // Regra: marca todas de uma vez — não uma por uma
    // Usado quando usuário abre o chat da ordem
    //   igual ao WhatsApp que marca todas como lidas
    //   quando você abre a conversa
    // Por que PATCH?
    //   Estamos atualizando o campo lida das mensagens
    //   não criando nem deletando recursos
    // No M7 o usuarioId virá do token JWT automaticamente
    @PatchMapping("/ordem/{ordemId}/lidas/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<Void>> marcarComoLida(
            @PathVariable UUID ordemId,
            @PathVariable UUID usuarioId) {
        messageService.marcarComoLida(ordemId, usuarioId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Mensagens marcadas como lidas",
                        HttpStatus.OK.value()
                ));
    }
}
