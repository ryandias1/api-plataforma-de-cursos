package br.com.ryan.api_cursos.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterLessonRequest(
    @NotNull int numLesson,
    @NotBlank String content,
    @NotNull UUID course_id
) {}