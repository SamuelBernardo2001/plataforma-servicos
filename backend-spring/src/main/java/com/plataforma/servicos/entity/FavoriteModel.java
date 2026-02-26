package com.plataforma.servicos.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FavoriteModel {

    private UUID id;

    private UUID userId;

    private UUID serviceId;

    private LocalDateTime criadoEm;
}
