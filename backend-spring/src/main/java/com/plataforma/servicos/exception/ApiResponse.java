package com.plataforma.servicos.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// @JsonInclude(NON_NULL) → campos nulos não aparecem no JSON
// Ex: em sucesso o campo "error" não aparece
// Ex: em erro o campo "data" não aparece
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
}
