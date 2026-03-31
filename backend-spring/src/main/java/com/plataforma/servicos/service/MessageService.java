package com.plataforma.servicos.service;

import com.plataforma.servicos.dto.messageDTOS.MessageRequestDTO;
import com.plataforma.servicos.dto.messageDTOS.MessageResponseDTO;
import com.plataforma.servicos.entity.MessageModel;
import com.plataforma.servicos.entity.OrderStatusEnum;
import com.plataforma.servicos.entity.ServiceOrderModel;
import com.plataforma.servicos.entity.UserModel;
import com.plataforma.servicos.mapper.MessageMapper;
import com.plataforma.servicos.repository.MessageRepository;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    // Lista mensagens de uma ordem com paginação
    // Igual ao WhatsApp — carrega as mensagens mais antigas primeiro
    // e vai carregando mais conforme o usuário sobe o scroll
    // Regra: apenas cliente ou prestador da ordem podem ver as mensagens
    // Regra: 20 mensagens por página — evita sobrecarga
    public Page<MessageResponseDTO> findByOrdem(UUID ordemId, UUID usuarioId, int pagina) {
        ServiceOrderModel ordem = serviceOrderRepository.findById(ordemId)
                .orElseThrow(() -> new RuntimeException("Ordem não encontrada"));

        // Apenas participantes da ordem veem as mensagens
        if (!ordem.getCliente().getId().equals(usuarioId) &&
                !ordem.getPrestador().getId().equals(usuarioId)) {
            throw new RuntimeException(
                    "Você não tem permissão para ver as mensagens desta ordem"
            );
        }

        // Paginação: 20 mensagens por página, ordenadas das mais antigas para mais recentes
        // Igual ao WhatsApp que mostra as mensagens em ordem cronológica
        PageRequest pageRequest = PageRequest.of(
                pagina,
                20, // 20 mensagens por página
                Sort.by("enviadoEm").ascending() // mais antigas primeiro
        );

        return messageRepository
                .findByServiceOrderIdOrderByEnviadoEmAsc(ordemId, pageRequest)
                .map(messageMapper::toResponseDTO);
    }

    // Envia mensagem vinculada a uma ordem
    // Regra: apenas cliente ou prestador da ordem podem enviar mensagens
    // Regra: não é possível enviar mensagem em ordem COMPLETED ou CANCELED
    // Regra: receptor é definido automaticamente —
    //        se remetente é cliente → receptor é prestador e vice-versa
    @Transactional
    public MessageResponseDTO send(UUID remetenteId, MessageRequestDTO dto) {
        UserModel remetente = userRepository.findById(remetenteId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        ServiceOrderModel ordem = serviceOrderRepository.findById(dto.serviceOrderId())
                .orElseThrow(() -> new RuntimeException("Ordem não encontrada"));

        // Valida se remetente é participante da ordem
        if (!ordem.getCliente().getId().equals(remetenteId) &&
                !ordem.getPrestador().getId().equals(remetenteId)) {
            throw new RuntimeException(
                    "Você não tem permissão para enviar mensagem nesta ordem"
            );
        }

        // Não permite mensagem em ordem finalizada
        // Faz sentido — se o serviço foi concluído ou cancelado não há mais o que conversar
        if (OrderStatusEnum.COMPLETED.equals(ordem.getStatus()) ||
                OrderStatusEnum.CANCELED.equals(ordem.getStatus())) {
            throw new RuntimeException("Não é possível enviar mensagem em ordem finalizada");
        }

        // Define receptor automaticamente baseado em quem está enviando
        // Se cliente envia → prestador recebe e vice-versa
        UserModel receptor = ordem.getCliente().getId().equals(remetenteId)
                ? ordem.getPrestador()
                : ordem.getCliente();

        MessageModel message = messageMapper.toModel(dto);
        message.setServiceOrder(ordem);
        message.setRemetente(remetente);
        message.setReceptor(receptor);
        message.setLer(false); // mensagem sempre começa como não lida
        message.setEditado(false); // mensagem não foi editada
        message.setEnviadoEm(LocalDateTime.now());

        return messageMapper.toResponseDTO(messageRepository.save(message));
    }

    // Edita mensagem
    // Regra: apenas o remetente pode editar sua própria mensagem
    // Regra: mensagem não pode ser deletada — apenas editada
    // Regra: marca como editada e registra quando foi editada
    //        igual ao WhatsApp que mostra "editada" na mensagem
    @Transactional
    public MessageResponseDTO edit(UUID messageId, UUID remetenteId, String novoConteudo) {
        MessageModel message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));

        // Apenas o remetente pode editar sua mensagem
        if (!message.getRemetente().getId().equals(remetenteId)) {
            throw new RuntimeException("Você não tem permissão para editar esta mensagem");
        }

        // Não permite editar mensagem em ordem finalizada
        if (OrderStatusEnum.COMPLETED.equals(message.getServiceOrder().getStatus()) ||
                OrderStatusEnum.CANCELED.equals(message.getServiceOrder().getStatus())) {
            throw new RuntimeException("Não é possível editar mensagem em ordem finalizada");
        }

        message.setConteudo(novoConteudo);
        message.setEditado(true); // Marca como editada — transparência para o receptor
        message.setEditadoEm(LocalDateTime.now()); // Registra quando foi editada

        return messageMapper.toResponseDTO(messageRepository.save(message));
    }

}
