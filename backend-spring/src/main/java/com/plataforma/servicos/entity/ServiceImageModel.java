package com.plataforma.servicos.entity;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServiceImageModel {

    private UUID id;

    private UUID serviceId;

    private String url;
}
