package br.com.ryan.api_cursos.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.ryan.api_cursos.dto.request.ModifyLessonRequest;
import br.com.ryan.api_cursos.dto.request.RegisterLessonRequest;
import br.com.ryan.api_cursos.dto.response.LessonResponse;
import br.com.ryan.api_cursos.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<LessonResponse> criar (@RequestBody @Valid RegisterLessonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.createLesson(request));
    }

    @PutMapping("/{id}/upload")
    public ResponseEntity<String> upload (@PathVariable UUID id ,@RequestParam("video") MultipartFile video) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(lessonService.uploadLesson(id, video));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> update (@PathVariable UUID id, @RequestBody @Valid ModifyLessonRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(lessonService.updateLesson(request, id));
    }
}
