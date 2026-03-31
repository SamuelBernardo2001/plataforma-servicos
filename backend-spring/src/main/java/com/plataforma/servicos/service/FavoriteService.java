package com.plataforma.servicos.service;

import com.plataforma.servicos.mapper.FavoriteMapper;
import com.plataforma.servicos.repository.FavoriteRepository;
import com.plataforma.servicos.repository.ServiceRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final FavoriteMapper favoriteMapper;

}
