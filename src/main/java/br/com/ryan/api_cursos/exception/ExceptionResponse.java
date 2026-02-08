package br.com.ryan.api_cursos.exception;

public record ExceptionResponse(
    String message,
    String status
) {}