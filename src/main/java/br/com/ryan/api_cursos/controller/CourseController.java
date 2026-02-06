package br.com.ryan.api_cursos.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ryan.api_cursos.dto.request.ModifyCourseRequest;
import br.com.ryan.api_cursos.dto.request.RegisterCourseRequest;
import br.com.ryan.api_cursos.dto.response.CourseResponse;
import br.com.ryan.api_cursos.enums.Category;
import br.com.ryan.api_cursos.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CourseResponse> register(@RequestBody @Valid RegisterCourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.registerCourse(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CourseResponse> modify (@RequestBody @Valid ModifyCourseRequest request, @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(courseService.modifyCourse(request, id));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAll (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourses(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourseById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<CourseResponse>> getByCategory(@PathVariable Category category) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getCoursesByCategory(category));
    }
}
