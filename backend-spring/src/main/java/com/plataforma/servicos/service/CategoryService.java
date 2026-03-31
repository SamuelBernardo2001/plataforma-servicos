package com.plataforma.servicos.service;

import com.plataforma.servicos.mapper.CategoryMapper;
import com.plataforma.servicos.repository.CategoryRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;
}
