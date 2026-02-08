package br.com.ryan.api_cursos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ryan.api_cursos.entity.Course;
import br.com.ryan.api_cursos.entity.Enrollment;
import br.com.ryan.api_cursos.entity.Student;
import br.com.ryan.api_cursos.entity.User;
import br.com.ryan.api_cursos.enums.EnrollmentStatus;
import br.com.ryan.api_cursos.repository.CourseRepository;
import br.com.ryan.api_cursos.repository.EnrollmentRepository;
import br.com.ryan.api_cursos.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private AuthService authService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    void deveMatricularAlunoComSucesso() {
        UUID userId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Student student = new Student();
        student.setUser(user);

        Course course = new Course();
        course.setId(courseId);

        when(authService.getLoggedUser()).thenReturn(user);
        when(studentRepository.findById(userId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentAndCourse(student, course))
                .thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = enrollmentService.enroll(courseId);

        assertEquals(EnrollmentStatus.ACTIVE, response.enrollmentStatus());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void naoDeveMatricularSeAlunoJaEstiverMatriculado() {
        UUID userId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Student student = new Student();
        student.setUser(user);

        Course course = new Course();
        course.setId(courseId);

        when(authService.getLoggedUser()).thenReturn(user);
        when(studentRepository.findById(userId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentAndCourse(student, course))
                .thenReturn(true);

        assertThrows(
                IllegalStateException.class,
                () -> enrollmentService.enroll(courseId)
        );
    }
}
