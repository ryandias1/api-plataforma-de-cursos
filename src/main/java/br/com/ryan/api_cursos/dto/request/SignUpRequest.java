package br.com.ryan.api_cursos.dto.request;

import br.com.ryan.api_cursos.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
    @NotBlank String name,
    @NotBlank String email,
    @NotBlank String password,
    @NotNull Role role
) {}