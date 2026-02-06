package br.com.ryan.api_cursos.dto.request;

import br.com.ryan.api_cursos.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterCourseRequest(
    @NotBlank String name,
    @NotBlank String description,
    @NotNull Category category
) {}