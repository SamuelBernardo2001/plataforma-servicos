package com.plataforma.servicos.service;

import com.plataforma.servicos.mapper.ReviewMapper;
import com.plataforma.servicos.repository.ReviewRepository;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;
}
