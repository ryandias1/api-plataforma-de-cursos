package br.com.ryan.api_cursos.dto.response;

import java.util.UUID;

import br.com.ryan.api_cursos.enums.Role;

public record SignUpResponse(
    UUID id,
    String name,
    String email,
    Role role
) {}