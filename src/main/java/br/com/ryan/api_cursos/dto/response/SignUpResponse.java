package br.com.ryan.api_cursos.dto.response;

import java.util.UUID;

public record SignUpResponse(
    UUID id,
    String name,
    String email
) {}