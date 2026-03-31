package com.plataforma.servicos.service;

import com.plataforma.servicos.mapper.MessageMapper;
import com.plataforma.servicos.repository.MessageRepository;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;
}
