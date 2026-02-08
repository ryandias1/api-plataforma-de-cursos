package br.com.ryan.api_cursos.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.ryan.api_cursos.enums.EnrollmentStatus;

public record EnrollmentResponse(
    UUID id,
    CourseResponse courseResponse,
    UserResponse user,
    LocalDateTime enrollmentTime,
    EnrollmentStatus enrollmentStatus
) {}