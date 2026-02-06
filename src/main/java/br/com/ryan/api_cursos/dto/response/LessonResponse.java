package br.com.ryan.api_cursos.dto.response;

import java.util.UUID;

public record LessonResponse(
    UUID id,
    int numLesson,
    String content,
    UUID course_id,
    String link
) {}