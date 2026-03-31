package com.plataforma.servicos.service;

import com.plataforma.servicos.mapper.ReportMapper;
import com.plataforma.servicos.repository.ReportRepository;
import com.plataforma.servicos.repository.ServiceOrderRepository;
import com.plataforma.servicos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ReportMapper reportMapper;
}
