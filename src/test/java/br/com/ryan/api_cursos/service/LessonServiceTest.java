package br.com.ryan.api_cursos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ryan.api_cursos.dto.request.ModifyLessonRequest;
import br.com.ryan.api_cursos.dto.request.RegisterLessonRequest;
import br.com.ryan.api_cursos.entity.Course;
import br.com.ryan.api_cursos.entity.Instructor;
import br.com.ryan.api_cursos.entity.Lesson;
import br.com.ryan.api_cursos.entity.User;
import br.com.ryan.api_cursos.repository.CourseRepository;
import br.com.ryan.api_cursos.repository.LessonRepository;
import br.com.ryan.api_cursos.video.MuxVideoService;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private MuxVideoService muxVideoService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private LessonService lessonService;

    @Test
    void deveCriarAulaQuandoInstrutorForDonoDoCurso() {
        UUID userId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Instructor instructor = new Instructor();
        instructor.setUser(user);

        Course course = new Course();
        course.setId(courseId);
        course.setInstructor(instructor);
        course.setLessons(new ArrayList<>());

        RegisterLessonRequest request =
                new RegisterLessonRequest(1, "Introdução", courseId);

        when(authService.getLoggedUser()).thenReturn(user);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = lessonService.createLesson(request);

        assertEquals(1, response.numLesson());
        assertEquals("Introdução", response.content());
        assertEquals(courseId, response.course_id());
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void deveAtualizarConteudoDaAulaQuandoInstrutorForDono() {
        UUID userId = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Instructor instructor = new Instructor();
        instructor.setUser(user);

        Course course = new Course();
        course.setInstructor(instructor);

        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setContent("Conteúdo antigo");
        lesson.setCourse(course);

        ModifyLessonRequest request =
                new ModifyLessonRequest("Conteúdo atualizado");

        when(authService.getLoggedUser()).thenReturn(user);
        when(lessonRepository.findById(lessonId))
                .thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = lessonService.updateLesson(request, lessonId);

        assertEquals("Conteúdo atualizado", response.content());
        verify(lessonRepository).save(lesson);
    }
}