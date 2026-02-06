package br.com.ryan.api_cursos.dto.response;

import java.util.UUID;

import br.com.ryan.api_cursos.enums.Category;

public record CourseResponse(
    UUID id,
    String name,
    String description,
    Category category,
    UserResponse instructor
) {}