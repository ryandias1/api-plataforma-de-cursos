package br.com.ryan.api_cursos.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
    @NotBlank String email,
    @NotBlank String password
) {}