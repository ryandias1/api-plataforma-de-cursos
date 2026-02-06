package br.com.ryan.api_cursos.dto.request;

import br.com.ryan.api_cursos.enums.Category;

public record ModifyCourseRequest(
    String name,
    String description,
    Category category
) {}