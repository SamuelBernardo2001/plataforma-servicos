package com.plataforma.servicos.service;

import com.plataforma.servicos.mapper.ServiceImageMapper;
import com.plataforma.servicos.repository.ServiceImageRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceImageService {

    private final ServiceImageRepository serviceImageRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ServiceImageMapper serviceImageMapper;
}
