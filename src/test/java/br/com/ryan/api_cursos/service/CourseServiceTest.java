package br.com.ryan.api_cursos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ryan.api_cursos.dto.request.RegisterCourseRequest;
import br.com.ryan.api_cursos.entity.Course;
import br.com.ryan.api_cursos.entity.Instructor;
import br.com.ryan.api_cursos.entity.User;
import br.com.ryan.api_cursos.enums.Category;
import br.com.ryan.api_cursos.enums.Role;
import br.com.ryan.api_cursos.repository.CourseRepository;
import br.com.ryan.api_cursos.repository.InstructorRepository;
import br.com.ryan.api_cursos.repository.LessonRepository;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private CourseService courseService;

    @Test
    void deveRegistrarCursoQuandoUsuarioForInstrutor() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setRole(Role.INSTRUCTOR);

        Instructor instructor = new Instructor();
        instructor.setUser(user);

        RegisterCourseRequest request =
                new RegisterCourseRequest("Java", "Curso Java", Category.BACKEND);

        when(authService.getLoggedUser()).thenReturn(user);
        when(instructorRepository.findById(userId)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = courseService.registerCourse(request);

        assertEquals("Java", response.name());
        assertEquals(Category.BACKEND, response.category());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void deveBuscarCursoPorId() {
        UUID courseId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setName("Ryan");
        user.setEmail("ryan@email.com");

        Instructor instructor = new Instructor();
        instructor.setUser(user);

        Course course = new Course();
        course.setId(courseId);
        course.setName("Spring Boot");
        course.setInstructor(instructor);

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.of(course));
        when(lessonRepository.findByCourse(course))
                .thenReturn(List.of());

        var response = courseService.getCourseById(courseId);

        assertEquals(courseId, response.id());
        assertEquals("Spring Boot", response.name());
        assertEquals("Ryan", response.instructor().name());
    }

    @Test
    void deveLancarErroQuandoCursoNaoExistir() {
        UUID id = UUID.randomUUID();

        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> courseService.getCourseById(id)
        );
    }
}