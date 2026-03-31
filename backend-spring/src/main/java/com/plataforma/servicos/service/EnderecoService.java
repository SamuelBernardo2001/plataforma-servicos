package com.plataforma.servicos.service;

import com.plataforma.servicos.mapper.EnderecoMapper;
import com.plataforma.servicos.repository.EnderecoRepository;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final UserRepository userRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final EnderecoMapper enderecoMapper;
}
